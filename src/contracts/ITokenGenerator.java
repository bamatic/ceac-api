package contracts;
/**
 * Generate signe JWT with the private key
 *
 */
public interface ITokenGenerator {
    /**
     * generate a signed JWT with the userId as subject
     *
     * @param userId the user identifier
     * @return  the signed JWT with the userId as subject
     *
     */
    String generateToken(String userId);
}
