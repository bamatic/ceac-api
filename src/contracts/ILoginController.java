package contracts;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface ILoginController {
    void login(HttpExchange exchange, String email, String password)  throws IOException;
}
