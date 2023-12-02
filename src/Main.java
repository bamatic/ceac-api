import com.sun.net.httpserver.HttpServer;
import configuration.Config;
import contracts.ILogger;
import http.Router;
import repositories.DataSourceProvider;
import services.impl1.LoggingService;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException  {

        Config.load();

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        DataSource sqlServer = DataSourceProvider.getSingleDataSource();

        ILogger logger = new LoggingService();
        logger.setLogFile("server-log", 50*1024*1024, 10);

        Router.addRoutes(httpServer, sqlServer, logger);

        httpServer.setExecutor(Executors.newCachedThreadPool());

        httpServer.start();
    }
}