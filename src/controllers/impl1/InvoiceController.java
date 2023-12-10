package controllers.impl1;

import com.sun.net.httpserver.HttpExchange;
import contracts.IHttpController;
import contracts.IJsonObject;
import contracts.ILogger;
import contracts.IRepository;
import entities.Command;
import entities.User;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class InvoiceController implements IHttpController {
    private final IRepository<Command> repo;
    public InvoiceController(IRepository<Command> repo) {
        this.repo = repo;
    }
    @Override
    public void setRepositoryUser(User user) {
        this.repo.setUser(user);
    }
    @Override
    public void index(User user, HttpExchange exchange, long limit) throws IOException {
        exchange.sendResponseHeaders(405, -1);
        exchange.close();
    }

    @Override
    public void show(User user, HttpExchange exchange, long id) throws IOException {
        Command command = this.repo.get(id);
        if (command == null) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
        }
        else {
            String responseBody = command.toXML();
            exchange.getResponseHeaders().set("Content-Type", "application/xml; charset=utf-8");
            exchange.sendResponseHeaders(200, responseBody.getBytes().length);
            OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
            writer.write(responseBody);
            writer.flush();
            exchange.close();
        }
    }

    @Override
    public void create(User user, HttpExchange exchange, IJsonObject payload) throws IOException {
        exchange.sendResponseHeaders(405, -1);
        exchange.close();
    }

    @Override
    public void update(User user, HttpExchange exchange, long id, IJsonObject payload) throws IOException {
        exchange.sendResponseHeaders(405, -1);
        exchange.close();
    }

    @Override
    public void delete(User user, HttpExchange exchange, long id) throws IOException {
        exchange.sendResponseHeaders(405, -1);
        exchange.close();
    }

}
