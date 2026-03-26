package com.taurupro.marketplace.infrastructure.security.jwt;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import org.springframework.beans.factory.annotation.Value;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class AwsCognitoRSAKeyProvider implements RSAKeyProvider {
    private final URL awsStoreUrl;

    private final JwkProvider provider;



    public AwsCognitoRSAKeyProvider(String awsConfigRegion,String identityPoolUrl,String jwtUrl){
        String url = String.format(jwtUrl,awsConfigRegion,identityPoolUrl);
        try {
            awsStoreUrl = new URL(url);
        }catch (MalformedURLException e){
            throw new RuntimeException(String.format("Invalid URL provided, URL=%S",url));
        }
        provider = new JwkProviderBuilder(awsStoreUrl).build();
    }

    @Override
    public RSAPublicKey getPublicKeyById(String kid) {
        try{
            return (RSAPublicKey) provider.get(kid).getPublicKey();
        }catch (JwkException e){
        throw new RuntimeException(String.format("failed to get jwt kid=%s from aws_kid_store_url=%s",kid,awsStoreUrl));
        }
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return null;
    }

    @Override
    public String getPrivateKeyId() {
        return "";
    }

}
