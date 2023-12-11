package contracts;

import com.sun.net.httpserver.HttpExchange;
import entities.User;
/**
 * verifies user credentials
 *
 */
public interface IUserAuthenticator {
    /**
     * verifies the credentials of a user
     *
     * @param email  the email
     * @param password the plain password
     * @return  the identification of the user or null
     *
     */
    long verify(String email, String password);
}
