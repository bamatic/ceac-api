package contracts;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
/**
 * verifies user credential and generates JSON WEB tokens
 *
 */
public interface ILoginController {
    /**
     * verifies user's credentials (email and password)
     * if verified a JWT is sent in the body of the HttpExchange
     * otherwise a 403 is sent
     *
     * @param exchange the HttpExchange object
     * @param email the user email to log in
     * @param password the user password
     */
    void login(HttpExchange exchange, String email, String password)  throws IOException;
}
