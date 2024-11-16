package com.hospital.infrastructure.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

/**
 * KeyPair -> RSAPublicKey
 */
@Component
public class KeyUtils {
    @Value("${jwt.key-path.access-token-private-path}")
    private String accessTokenPrivateKeyPath;
    @Value("${jwt.key-path.access-token-public-path}")
    private String accessTokenPublicKeyPath;

    @Value("${jwt.key-path.refresh-token-private-path}")
    private String refreshTokenPrivateKeyPath;
    @Value("${jwt.key-path.refresh-token-public-path}")
    private String refreshTokenPublicKetPath;

    private KeyPair _accessTokenKeyPair;
    private KeyPair _refreshTokenKeyPair;
    /**
     * Get RSAPublicKey for AccessToken form AccessTokenKeyPair
     *
     * @return RSAPublicKey
     */
    public RSAPublicKey getAccessTokenPublicKey() {
        return (RSAPublicKey) getAccessTokenKeyPair().getPublic();
    }
    /**
     * Get RSAPrivateKey for AccessToken form AccessTokenKeyPair
     *
     * @return RSAPrivateKey
     */
    public RSAPrivateKey getAccessTokenPrivateKey() {
        return (RSAPrivateKey) getAccessTokenKeyPair().getPrivate();
    }
    /**
     * Get RSAPublicKey for RefreshToken form RefreshTokenKeyPair
     *
     * @return RSAPublicKey
     */
    public RSAPublicKey getRefreshTokenPublicKey() {
        return (RSAPublicKey) getRefreshTokenKeyPair().getPublic();
    }
    /**
     * Get RSAPrivateKey for RefreshToken form RefreshTokenKeyPair
     *
     * @return RSAPrivateKey
     */
    public RSAPrivateKey getRefreshTokenPrivateKey() {
        return (RSAPrivateKey) getRefreshTokenKeyPair().getPrivate();
    }

    /**
     * Get KeyPair for AccessToken
     * @return KeyPair
     */
    private KeyPair getAccessTokenKeyPair() {
        if (Objects.isNull(_accessTokenKeyPair)) {
            _accessTokenKeyPair = getKeyPair(accessTokenPublicKeyPath, accessTokenPrivateKeyPath);
        }
        return _accessTokenKeyPair;
    }
    /**
     * Get KeyPair for RefreshToken
     * @return KeyPair
     */
    private KeyPair getRefreshTokenKeyPair() {
        if (Objects.isNull(_refreshTokenKeyPair)) {
            _refreshTokenKeyPair = getKeyPair(refreshTokenPublicKetPath, refreshTokenPrivateKeyPath);
        }
        return _refreshTokenKeyPair;
    }

    /**
     * Get KeyPair from publicKeyPath and privateKeyPath
     * @param publicKeyPath
     * @param privateKeyPath
     * @return KeyPair
     */
    private KeyPair getKeyPair(String publicKeyPath, String privateKeyPath) {
        /*
         Get file include public key and private key
         */
        File publicKeyFile = new File(publicKeyPath);
        File privateKeyFile = new File(privateKeyPath);

        //Check exists files
        if (publicKeyFile.exists() && privateKeyFile.exists()) {
            try {
                /*
                New a key factory to generate public/private key from file
                with:
                    -X509EncodedKeySpec -> public key
                    -PKCS8EncodedKeySpec-> private key
                 */
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
                EncodedKeySpec publicEncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                PublicKey publicKey = keyFactory.generatePublic(publicEncodedKeySpec);

                byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
                EncodedKeySpec privateEncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                PrivateKey privateKey = keyFactory.generatePrivate(privateEncodedKeySpec);
                return new KeyPair(publicKey, privateKey);
            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Don't read public file & private file");
        }
        File directory = new File("keys");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                fos.write(keySpec.getEncoded());
            }
            try (FileOutputStream fos = new FileOutputStream(privateKeyPath)) {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
                fos.write(keySpec.getEncoded());
            }
            return keyPair;
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
