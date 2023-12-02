package services.impl1.jwt;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class RSAPublicKeyService {
    public static PublicKey getPublicKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        String publicKeyPem = PEMFileService.getKey(filename);
        publicKeyPem = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPem = publicKeyPem.replace("-----END PUBLIC KEY-----", "");
        byte[] encoded = Base64.decodeBase64(publicKeyPem);


        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }
}
