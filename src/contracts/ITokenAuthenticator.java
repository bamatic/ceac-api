package contracts;

import com.sun.net.httpserver.HttpExchange;
import entities.User;
/**
 * verifies the signature of the JWT sent to the API
 *
 */
public interface ITokenAuthenticator {
    /**
     * verifies the signature of the JWT sent to the API
     *
     * @param exchange the Http Exchange
     * @param userRepo to access user data
     * @return  the authenticated user or null
     *
     */
    User verify(HttpExchange exchange, IRepository<User> userRepo);
}
