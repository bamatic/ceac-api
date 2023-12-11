package services.impl1.jwt;

import com.sun.net.httpserver.HttpExchange;
import configuration.Config;
import contracts.ILogger;
import contracts.ITokenAuthenticator;
import contracts.IRepository;
import entities.User;
import http.HttpRequestManager;
import io.jsonwebtoken.*;

import java.io.IOException;
import java.net.HttpCookie;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class JwtAuthenticationService implements ITokenAuthenticator {

    private final ILogger logger;

    public JwtAuthenticationService(ILogger logger) {
        this.logger = logger;
    }
    @Override
    public User verify(HttpExchange exchange, IRepository<User> userRepo) {
        String jws = TokenExtractorService.getToken(exchange, "bamatic-bearer");
        if (jws == null) {
            if ( !(new HttpRequestManager(exchange, this.logger)).getMethod().equals("OPTIONS"))
                logger.message(
                        "INFO",
                        "No jwt found"
                );
            
            return null;
        }
        try {
            PublicKey publicKey = RSAPublicKeyService.getPublicKey(Config.publicKeyPath);
            JwtParserBuilder parserBuilder = Jwts.parser();
            try {
                parserBuilder = parserBuilder.verifyWith(publicKey);
            }
            catch (JwtException e) {
                logger.message(
                        "SEVERE",
                        "JwtException Error while reading the public key: " + e.getMessage()
                );
                
                return null;
            }
            JwtParser jwtParser = parserBuilder.build();
            try {
                Jws<Claims> claimsJws = jwtParser.parseSignedClaims(jws);
            }
            catch (JwtException e) {
                logger.message(
                        "SEVERE",
                        "JwtException while verifying jwt signature: " + e.getMessage()
                );
                
                return null;
            }
            String userId =  Jwts
                    .parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(jws)
                    .getPayload()
                    .getSubject();
            try {
                long lUserId = Long.parseLong(userId);
                userRepo.setUser(new User(lUserId));
                User user = userRepo.get(lUserId);
                return user;
            }
            catch (NumberFormatException e) {
                logger.message(
                        "SEVERE",
                        "Jwt subject is not an integer: " + e.getMessage()
                );
                
                return null;
            }

        } catch (IOException e) {
            logger.message(
                    "SEVERE",
                    "IO Error while reading the public key: " + e.getMessage()
            );
            
            return null;
        } catch (NoSuchAlgorithmException e) {
            logger.message(
                    "SEVERE",
                    "NoSuchAlgorithmException while reading the public key: " + e.getMessage()
            );
            
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            logger.message(
                    "SEVERE",
                    "InvalidKeySpecException while reading the public key: " + e.getMessage()
            );
            
            return null;
        }

    }
}
