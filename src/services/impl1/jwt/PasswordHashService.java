package services.impl1.jwt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PasswordHashService {
    public static byte[] hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 1024, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");


        return factory.generateSecret(spec).getEncoded();
    }

    private static final byte[] salt = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
}
