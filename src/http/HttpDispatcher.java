package http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import contracts.*;
import entities.User;
import configuration.Config;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collection;
import java.util.List;


/**
 * creates the contexts for the HttpServer
 * associating a controller to the context
 * associating to the HTTP VERB a controller method
 * POST --> create, PUT --> update, DELETE --> delete
 * GET  --> show or GET  --> all
 */
public class HttpDispatcher {
    /**
     * create a context into the HttpServer
     * and calls the controller method
     *
     * @param server the HttpServer instance
     * @param route the context name
     * @param controller the controller to manage requests to this context
     * @param authenticator the JWT authentication service
     * @param logger for logging
     * @param userRepo accessing the user using the JWT subject
     *
     */
    public static void dispatch(
            HttpServer server,
            String route,
            IHttpController controller,
            ITokenAuthenticator authenticator,
            ILogger logger,
            IRepository<User> userRepo
    ) {
        server.createContext(route, exchange -> {
            Headers headers = exchange.getResponseHeaders();
            String origin = exchange.getRequestHeaders().getFirst("Origin");
            if (origin == null) origin = "*";
            headers.add("Access-Control-Allow-Origin", origin);
            headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            headers.add("Access-Control-Allow-Headers", "X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization, Cookie, Set-Cookie");
            headers.add("Access-Control-Allow-Credentials", "true");

            User user = authenticator.verify(exchange, userRepo);
            if (user != null || exchange.getRequestMethod().equals("OPTIONS")) {
                controller.setRepositoryUser(user);

                HttpRequestManager httpRequestManager = new HttpRequestManager(exchange, logger);
                String method = httpRequestManager.getMethod();
                long id = httpRequestManager.getId();
                IJsonObject payload = httpRequestManager.getPayload();

                switch (method) {
                    case "GET":
                        if (id > 0) {
                            try {
                                controller.show(user, exchange, id);
                            }
                            catch (IOException e) {
                                logger.message(
                                        "SEVERE",
                                        "IO Error while treating login HTTP request: " + e.getMessage()
                                );
                                
                                throw new RuntimeException(e);
                            }
                        } else if (id == 0) {
                            long limit = httpRequestManager.getLimit();
                            try {
                                controller.index(user, exchange, limit);
                            }
                            catch (IOException e) {
                                logger.message(
                                        "SEVERE",
                                        "IO Error while treating login HTTP request: " + e.getMessage()
                                );
                                
                                throw new RuntimeException(e);
                            }
                        } else {
                            exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
                            exchange.close();
                        }
                        break;
                    case "POST":
                        if (id == 0 && payload != null) {
                            try {
                                controller.create(user, exchange, payload);
                            }
                            catch (IOException e) {
                                logger.message(
                                        "SEVERE",
                                        "IO Error while treating login HTTP request: " + e.getMessage()
                                );
                                
                                throw new RuntimeException(e);
                            }
                        } else {
                            exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
                            exchange.close();
                        }
                        break;
                    case "PUT":
                        if (id > 0 && payload != null) {
                            try {
                                controller.update(user, exchange, id, payload);
                            }
                            catch (IOException e) {
                                logger.message(
                                        "SEVERE",
                                        "IO Error while treating login HTTP request: " + e.getMessage()
                                );
                                
                                throw new RuntimeException(e);
                            }
                            exchange.close();
                        } else {
                            exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
                            exchange.close();
                        }
                    case "DELETE":
                        if (id > 0 && payload == null) {
                            try {
                                controller.delete(user, exchange, id);
                            }
                            catch (IOException e) {
                                logger.message(
                                        "SEVERE",
                                        "IO Error while treating login HTTP request: " + e.getMessage()
                                );
                                
                                throw new RuntimeException(e);
                            }
                        } else {
                            exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
                            exchange.close();
                        }
                    case "OPTIONS":
                        exchange.sendResponseHeaders(200, -1);
                        exchange.close();

                }
            }
            else {
                exchange.sendResponseHeaders(403, -1);
                exchange.close();
            }
        });
    }
    /**
     * create a context into the HttpServer
     * and calls the controller method
     *
     * @param server the HttpServer instance
     * @param route the context name
     * @param loginController the controller to manage requests to this context
     * @param logger for logging
     *
     */
    public static void login(
            HttpServer server,
            String route,
            ILoginController loginController,
            ILogger logger
    ) {
        server.createContext(route, exchange -> {
            Headers headers = exchange.getResponseHeaders();
            Headers refererHeaders = exchange.getRequestHeaders();
            String origin = refererHeaders.getFirst("Origin");
            if (origin == null) origin = "*";
            headers.add("Access-Control-Allow-Origin", origin);
            headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            headers.add("Access-Control-Allow-Headers", "X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization, Cookie, Set-Cookie");
            headers.add("Access-Control-Allow-Credentials", "true");

            HttpRequestManager httpRequestManager = new HttpRequestManager(exchange, logger);
            String method = httpRequestManager.getMethod();
            long id = httpRequestManager.getId();
            IJsonObject payload = httpRequestManager.getPayload();
            if (payload != null) {
                String email = (String) payload.get("email");
                String password = (String) payload.get("password");
                if (method.equals("POST") && id == 0 && email != null && password != null) {
                    try {
                        loginController.login(exchange, email, password);
                    }
                    catch (IOException e) {
                        logger.message(
                                "SEVERE",
                                "IO Error while treating login HTTP request: " + e.getMessage()
                        );
                        
                        throw new RuntimeException(e);
                    }

                }
            }
            else if (method.equals("OPTIONS")) {
                exchange.sendResponseHeaders(200, -1);
                exchange.close();
            }
            else {
                exchange.sendResponseHeaders(400, -1);
                exchange.close();
            }
        });

    }
}
