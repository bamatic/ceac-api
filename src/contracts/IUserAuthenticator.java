package contracts;

import com.sun.net.httpserver.HttpExchange;
import entities.User;

public interface IUserAuthenticator {
    long verify(String email, String password);
}
