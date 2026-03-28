package com.taurupro.marketplace.domain.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taurupro.marketplace.domain.dto.*;
import com.taurupro.marketplace.domain.enums.UserRole;
import com.taurupro.marketplace.domain.repository.SupplierRepository;
import com.taurupro.marketplace.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final AWSCognitoIdentityProvider awsCognitoIdentityProvider;

    private final SupplierRepository supplierRepository;

    @Value(value = "${aws.cognito.clientId}")
    private String clientId;

    @Value(value = "${aws.cognito.userPoolId}")
    private String userPoolId;

    public UserService(UserRepository userRepository, AWSCognitoIdentityProvider awsCognitoIdentityProvider, SupplierRepository supplierRepository) {
        this.userRepository = userRepository;
        this.awsCognitoIdentityProvider = awsCognitoIdentityProvider;
        this.supplierRepository = supplierRepository;
    }

    public Page<UserDto> getAll(int page, int elements){
        return this.userRepository.getAll(page, elements);
    }

    public void createUser(UserDto userDto) {
        this.userRepository.save(userDto);
    }

    @Transactional
    public void signUpUser(SignUpUser user) {
        // 1. Crear usuario en Cognito con contraseña temporal
        String cognitoSub = createCognitoUserWithTempPassword(user);
        // 2. Persistir usuario en base de datos
        UserDto userDto = new UserDto(
                null,
                user.email(),
                user.name(),
                user.lastName(),
                cognitoSub,
                user.role(),
                true,
                null
        );
        createUser(userDto);

        // 3. Si es SUPPLIER, registrar datos del proveedor
        if (user.role() == UserRole.SUPPLIER) {
            registerSupplier(user);
        }
    }
  public void signUpCustomer(SingUpCustomerDto user) {
        // 1. Crear usuario en Cognito con contraseña temporal
        String cognitoSub = createCognitoCustomer(user);
        // 2. Persistir usuario en base de datos
        UserDto userDto = new UserDto(
                null,
                user.email(),
                user.name(),
                user.lastName(),
                cognitoSub,
                UserRole.CUSTOMER,
                true,
                null
        );
        createUser(userDto);

    }

    public void createAdminUser(SignUpAdmin admin) {
        // 1. Crear en Cognito con contraseña DEFINITIVA (no temporal)
        String cognitoSub = createCognitoAdminUser(admin);

        // 2. Persistir en base de datos con rol ADMIN
        UserDto userDto = new UserDto(
                null,
                admin.email(),
                admin.name(),
                admin.lastName(),
                cognitoSub,
                UserRole.ADMIN,
                true,
                null
        );
        createUser(userDto);
    }

    public AuthResponseDto signInUser(LoginDto loginDto) {
        final Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", loginDto.email());
        authParams.put("PASSWORD", loginDto.password());

        try {
            InitiateAuthRequest request = new InitiateAuthRequest()
                    .withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
                    .withClientId(clientId)
                    .withAuthParameters(authParams);

            InitiateAuthResult authResult =
                    awsCognitoIdentityProvider.initiateAuth(request);
            UserAuthDto user = getUserAuthDto(loginDto.email());
            if ("NEW_PASSWORD_REQUIRED".equals(authResult.getChallengeName())) {
                return new AuthResponseDto(
                        "NEW_PASSWORD_REQUIRED",
                        null,
                        null, authResult.getSession(),null
                );
            }

            return new AuthResponseDto(
                    "SUCCESS",
                    authResult.getAuthenticationResult().getAccessToken(),
                    authResult.getAuthenticationResult().getRefreshToken(),
                    null,user

            );

        } catch (NotAuthorizedException e) {
            throw new RuntimeException("Credenciales inválidas");
        }
    }


    private UserAuthDto getUserAuthDto(String email) {
        UserDto userDto =  findByEmail(email) .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new UserAuthDto(userDto.name(),userDto.lastName(),userDto.role());
    }

    public AuthResponseDto refreshToken(String refreshToken) {
        final Map<String, String> authParams = new HashMap<>();
        authParams.put("REFRESH_TOKEN", refreshToken);

        try {
            InitiateAuthRequest request = new InitiateAuthRequest()
                    .withAuthFlow(AuthFlowType.REFRESH_TOKEN_AUTH) // 👈 Flujo de refresco
                    .withClientId(clientId)
                    .withAuthParameters(authParams);

            InitiateAuthResult authResult =
                    awsCognitoIdentityProvider.initiateAuth(request);

            AuthenticationResultType result = authResult.getAuthenticationResult();

            // Cognito no devuelve un nuevo refresh token siempre,
            // se reutiliza el mismo si no viene uno nuevo
            String newRefreshToken = result.getRefreshToken() != null
                    ? result.getRefreshToken()
                    : refreshToken;

            // Extraer el email del access token para buscar el usuario
            String cognitoSub = extractCognitoSubFromToken(result.getAccessToken());
          UserDto userDto =  findByCognitoSub(cognitoSub).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            return new AuthResponseDto(
                    "SUCCESS",
                    result.getAccessToken(),
                    newRefreshToken,
                    null,new UserAuthDto(userDto.name(),userDto.lastName(),userDto.role())

            );

        } catch (NotAuthorizedException e) {
            throw new RuntimeException("Refresh token inválido o expirado");
        }
    }

    public AuthResponseDto confirmNewPassword(ConfirmPasswordDto dto) {
        Map<String, String> challengeResponses = new HashMap<>();
        challengeResponses.put("USERNAME", dto.email());
        challengeResponses.put("NEW_PASSWORD", dto.newPassword());

        AdminRespondToAuthChallengeRequest challengeRequest = new AdminRespondToAuthChallengeRequest()
                .withUserPoolId(userPoolId)
                .withClientId(clientId)
                .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                .withChallengeResponses(challengeResponses)
                .withSession(dto.session());

        AdminRespondToAuthChallengeResult result =
                awsCognitoIdentityProvider.adminRespondToAuthChallenge(challengeRequest);

        UserAuthDto user = getUserAuthDto(dto.email());

        return new AuthResponseDto(
                "SUCCESS",
                result.getAuthenticationResult().getAccessToken(),
                result.getAuthenticationResult().getRefreshToken(), null,
                user
        );
    }

    public Optional<UserDto> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public Optional<UserDto> findByCognitoSub(String sub) {
        return this.userRepository.findByCognitoSub(sub);
    }

    private void registerSupplier(SignUpUser user) {
        CreateSupplierDto data = user.supplier();
        if (data == null) {
            throw new IllegalArgumentException(
                    "Supplier data is required when role is SUPPLIER"
            );
        }

        // Recuperar el userId recién creado
        UUID userId = userRepository.findByEmail(user.email())
                .map(UserDto::id)
                .orElseThrow(() -> new IllegalStateException("User not found after creation"));

        SupplierDto supplier = new SupplierDto(data.nit(), user.email(), data.phone(), data.legalName(), null,userId, null);

        supplierRepository.save(supplier);
    }
    private String createCognitoCustomer(SingUpCustomerDto user) {
        AttributeType emailAttribute = new AttributeType()
                .withName("email")
                .withValue(user.email());

        AttributeType emailVerifiedAttribute = new AttributeType()
                .withName("email_verified")
                .withValue("true");

        AttributeType firstNameAttribute = new AttributeType()
                .withName("given_name")
                .withValue(user.name()); // o como se llame en tu DTO

        AttributeType lastNameAttribute = new AttributeType()
                .withName("family_name")
                .withValue(user.lastName());

        AdminCreateUserRequest request = new AdminCreateUserRequest()
                .withUserPoolId(userPoolId)
                .withUsername(user.email())

                .withMessageAction("SUPPRESS")
                .withUserAttributes(
                        emailAttribute,
                        emailVerifiedAttribute,
                        firstNameAttribute,
                        lastNameAttribute);

        AdminCreateUserResult createUserResult = awsCognitoIdentityProvider.adminCreateUser(request);
        // 2. Asignar contraseña definitiva
        AdminSetUserPasswordRequest passwordRequest = new AdminSetUserPasswordRequest()
                .withUserPoolId(userPoolId)
                .withUsername(user.email())
                .withPassword(user.password())
                .withPermanent(true);
        awsCognitoIdentityProvider.adminSetUserPassword(passwordRequest);
        return createUserResult.getUser().getAttributes().stream()
                .filter(attr -> attr.getName().equals("sub"))
                .findFirst()
                .map(AttributeType::getValue)
                .orElseThrow(() -> new IllegalStateException("Sub not found after user creation"));
    }

    private String createCognitoUserWithTempPassword(SignUpUser user) {
        AttributeType emailAttribute = new AttributeType()
                .withName("email")
                .withValue(user.email());

        AttributeType emailVerifiedAttribute = new AttributeType()
                .withName("email_verified")
                .withValue("true");

        AttributeType firstNameAttribute = new AttributeType()
                .withName("given_name")
                .withValue(user.name()); // o como se llame en tu DTO

        AttributeType lastNameAttribute = new AttributeType()
                .withName("family_name")
                .withValue(user.lastName());

        AdminCreateUserRequest createUserRequest = new AdminCreateUserRequest()
                .withUserPoolId(userPoolId)
                .withUsername(user.email())
                .withTemporaryPassword(generateTemporaryPassword())
                .withUserAttributes(
                        emailAttribute,
                        emailVerifiedAttribute,
                        firstNameAttribute,
                        lastNameAttribute);

        AdminCreateUserResult createUserResult = awsCognitoIdentityProvider.adminCreateUser(createUserRequest);

        return createUserResult.getUser().getAttributes().stream()
                .filter(attr -> attr.getName().equals("sub"))
                .findFirst()
                .map(AttributeType::getValue)
                .orElseThrow(() -> new IllegalStateException("Sub not found after user creation"));
    }

    private String generateTemporaryPassword() {
        // Genera password que cumpla política de Cognito: mayúscula, número, símbolo
        return "Temp@" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String createCognitoAdminUser(SignUpAdmin admin) {
        AttributeType emailAttribute = new AttributeType()
                .withName("email")
                .withValue(admin.email());

        AttributeType emailVerifiedAttribute = new AttributeType()
                .withName("email_verified")
                .withValue("true");

        // Crear usuario con contraseña temporal primero
        AdminCreateUserRequest createUserRequest = new AdminCreateUserRequest()
                .withUserPoolId(userPoolId)
                .withUsername(admin.email())
                .withTemporaryPassword(admin.password())
                .withUserAttributes(emailAttribute, emailVerifiedAttribute)
                .withMessageAction(MessageActionType.SUPPRESS);

        AdminCreateUserResult createUserResult = awsCognitoIdentityProvider.adminCreateUser(createUserRequest);

        // Establecer la contraseña como PERMANENTE (evita el forced change en primer login)
        AdminSetUserPasswordRequest setPasswordRequest = new AdminSetUserPasswordRequest()
                .withUserPoolId(userPoolId)
                .withUsername(admin.email())
                .withPassword(admin.password())
                .withPermanent(true);  // <-- clave: no pedirá cambio de contraseña

        awsCognitoIdentityProvider.adminSetUserPassword(setPasswordRequest);
        return createUserResult.getUser().getAttributes().stream()
                .filter(attr -> attr.getName().equals("sub"))
                .findFirst()
                .map(AttributeType::getValue)
                .orElseThrow(() -> new IllegalStateException("Sub not found after admin creation"));
    }

    private String extractCognitoSubFromToken(String accessToken) {
        String[] parts = accessToken.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        // Usa ObjectMapper o JsonParser según tu proyecto
        try {
            JsonNode node = new ObjectMapper().readTree(payload);
            return node.get("username").asText(); // campo en Cognito
        } catch (Exception e) {
            throw new RuntimeException("Error al decodificar el token");
        }
    }
}
