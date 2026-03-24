package com.taurupro.marketplace.domain.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.taurupro.marketplace.domain.dto.*;
import com.taurupro.marketplace.domain.enums.UserRole;
import com.taurupro.marketplace.domain.repository.SupplierRepository;
import com.taurupro.marketplace.domain.repository.UserRepository;
import com.taurupro.marketplace.persistence.entity.SupplierEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    public void createUser(UserDto userDto) {
        this.userRepository.save(userDto);
    }

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
                true
        );
        createUser(userDto);

        // 3. Si es SUPPLIER, registrar datos del proveedor
        if (user.role() == UserRole.SUPPLIER) {
            registerSupplier(user);
        }
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
                true
        );
        createUser(userDto);
    }

    public AuthResponseDto  signInUser(LoginDto loginDto) {
        final Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", loginDto.email());
        authParams.put("PASSWORD", loginDto.password());

        InitiateAuthRequest initiateAuthRequest = new InitiateAuthRequest()
                .withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .withClientId(clientId)
                .withAuthParameters(authParams);
        InitiateAuthResult authResult = awsCognitoIdentityProvider.initiateAuth(initiateAuthRequest);
        System.out.println(authResult.getChallengeName());
        if ("NEW_PASSWORD_REQUIRED".equals(authResult.getChallengeName())) {
            return new AuthResponseDto(
                    "NEW_PASSWORD_REQUIRED",
                    null,
                    authResult.getSession()  // el frontend debe guardar esta session
            );
        }

        // Login exitoso
        return new AuthResponseDto(
                "SUCCESS",
                authResult.getAuthenticationResult().getAccessToken(),
                null
        );
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

        return new AuthResponseDto(
                "SUCCESS",
                result.getAuthenticationResult().getAccessToken(),
                null
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

        SupplierDto supplier = new SupplierDto(data.nit(), user.email(), data.phone(), data.legalName(), userId);

        supplierRepository.save(supplier);
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
}
