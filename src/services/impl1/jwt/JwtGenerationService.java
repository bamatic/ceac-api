package services.impl1.jwt;

import contracts.ILogger;
import contracts.ITokenGenerator;
import io.jsonwebtoken.Jwts;

import java.io.IOException;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

public class JwtGenerationService implements ITokenGenerator {
    private final long expirationTimeInMillis;
    private final ILogger logger;

    public JwtGenerationService(long expirationTimeInMillis, ILogger logger) {
        this.expirationTimeInMillis = expirationTimeInMillis;
        this.logger = logger;
    }

    public String generateToken(String userId) {
        try {
            RSAPrivateKey privateKey = RSAPrivateKeyService.getPrivateKey();
            Date now = new Date();
            long expired_at = now.getTime() + this.expirationTimeInMillis;
            Date expiration = new Date(expired_at);
            return Jwts.builder()
                    .issuer("bamatic")
                    .issuedAt(now)
                    .expiration(expiration)
                    .subject(userId)
                    .signWith(privateKey)
                    .compact();
        } catch (IOException e) {
            logger.message(
                    "SEVERE",
                    "IO Error while reading the private key: " + e.getMessage()
            );
            
            return null;
        } catch (NoSuchAlgorithmException e) {
            logger.message(
                    "SEVERE",
                    "NoSuchAlgorithmException while reading the private key: " + e.getMessage()
            );
            
            return null;
        } catch (InvalidKeySpecException e) {
            logger.message(
                    "SEVERE",
                    "InvalidKeySpecException while reading the private key: " + e.getMessage()
            );
            
            return null;
        }
    }
}
