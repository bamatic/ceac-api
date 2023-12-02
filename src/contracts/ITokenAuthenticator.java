package contracts;

import com.sun.net.httpserver.HttpExchange;
import entities.User;

public interface ITokenAuthenticator {
    User verify(HttpExchange exchange, IRepository<User> userRepo);
}
