package controllers.impl1;

import com.sun.net.httpserver.HttpExchange;
import contracts.*;
import services.impl1.JSON.JsonObjectService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;

public class LoginController implements ILoginController {
    private final IUserAuthenticator userAuthenticator;
    private final ITokenGenerator tokenGenerator;

    public LoginController(IUserAuthenticator userAuthenticator, ITokenGenerator tokenGenerator) {
        this.userAuthenticator = userAuthenticator;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public void login(HttpExchange exchange, String email, String password)  throws IOException {
        long userId = this.userAuthenticator.verify(email, password);
        if  (userId > 0) {
            IJsonObject token = new JsonObjectService();
            token.put("bamatic-bearer", this.tokenGenerator.generateToken(String.valueOf(userId)));
            String responseBody = token.toJSONString();
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, responseBody.getBytes().length);
            OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
            writer.write(responseBody);
            writer.flush();
            exchange.close();
        }
        else {
            exchange.sendResponseHeaders(403, -1);
            exchange.close();
        }
    }
}
