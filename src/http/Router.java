package http;

import com.sun.net.httpserver.HttpServer;
import contracts.*;
import controllers.impl1.*;
import entities.User;
import repositories.impl1.*;
import services.impl1.AuthenticationService;
import services.impl1.jwt.JwtAuthenticationService;
import services.impl1.jwt.JwtGenerationService;

import javax.sql.DataSource;

/**
 * provisioning of dependencies and dispatch
 * of requests to the correct controller
 */
public class Router {
    /**
     * all contexts (routes) are registered here
     *
     * @param server the HttpServer instance
     * @param dataSource the JDBC datasource
     * @param logger for logging
     *
     */
    public static void addRoutes(HttpServer server, DataSource dataSource, ILogger logger) {

        ITokenAuthenticator jwtService = new JwtAuthenticationService(logger);
        IRepository<User> userRepository = new UserRepository(dataSource, logger);

        IHttpController commandController = new CommandController(
                new CommandRepository(dataSource, logger),
                new CommandFromShoppingCard(dataSource, logger),
                logger
        );
        HttpDispatcher.dispatch(server,"/commands/", commandController,jwtService, logger, userRepository);

        IHttpController productController = new ProductController(
                new ProductRepository(dataSource, logger)
        );
        HttpDispatcher.dispatch(server,"/products/", productController,jwtService, logger, userRepository);

        IHttpController addressController = new AddressController(
                new AddressRepository(dataSource, logger),
                new AddressUserRepository(dataSource, logger),
                logger
        );
        HttpDispatcher.dispatch(server, "/addresses/", addressController, jwtService, logger, userRepository);

        IHttpController invoiceController = new InvoiceController(
                new CommandRepository(dataSource, logger)
        );
        HttpDispatcher.dispatch(server, "/invoices/", invoiceController, jwtService, logger, userRepository);

        IHttpController shoppingCardController = new ShoppingCardController(
                new ShoppingCardRepository(dataSource, logger)
        );
        HttpDispatcher.dispatch(server,"/shopping-card/", shoppingCardController,jwtService, logger, userRepository);

        IHttpController userController = new UserController(
                new UserRepository(dataSource, logger)
        );
        HttpDispatcher.dispatch(server, "/users/",userController, jwtService, logger, userRepository);


        ILoginController loginController = new LoginController(
                new AuthenticationService(new UserPasswordRepository(dataSource, logger)),
                new JwtGenerationService(60*60*1000, logger)
        );
        HttpDispatcher.login(server, "/login/", loginController, logger);
    }
}
