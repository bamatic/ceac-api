package controllers;

import com.sun.net.httpserver.HttpExchange;
import contracts.*;
import entities.Command;
import entities.User;
import http.HttpRequestManager;
import services.impl1.JSON.JsonArrayService;
import services.impl1.JSON.JsonObjectService;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
public class CommandController implements IHttpController {

    private final IRepository<Command> repo;
    private final ICommandFromShoppingCard shoppingCardRepo;
    private final ILogger logger;
    public CommandController(IRepository<Command> repo, ICommandFromShoppingCard shoppingCardRepo, ILogger logger) {
        this.repo = repo;
        this.shoppingCardRepo = shoppingCardRepo;
        this.logger = logger;
    }
    @Override
    public void setRepositoryUser(User user) {
        this.repo.setUser(user);
        this.shoppingCardRepo.setUser(user);
    }
    @Override
    public void index(User user, HttpExchange exchange, long limit) throws IOException {
        List<Command> commands = this.repo.all(limit);
        IJsonArray jsonCommands = new JsonArrayService();
        for (Command command : commands) {
            IJsonObject json = command.toJSONObject();
            jsonCommands.add(json);
        }
        String responseBody = jsonCommands.toJSONString();
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, responseBody.getBytes().length);
        OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
        writer.write(responseBody);
        writer.flush();
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
            IJsonObject jsonCommand = command.toJSONObject();
            String responseBody = jsonCommand.toJSONString();
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, responseBody.getBytes().length);
            OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
            writer.write(responseBody);
            writer.flush();
            exchange.close();
        }

    }

    @Override
    public void create(User user, HttpExchange exchange, IJsonObject payload) throws IOException {
        Command command = Command.fromJSONObject(user, payload);
        if (command != null) {
            Command newCommand = null;
            HttpRequestManager httpRequestManager = new HttpRequestManager(exchange, this.logger);
            if (httpRequestManager.isDeleteSP()) {
                newCommand = this.shoppingCardRepo.createSP(command);
            }
            else {
                newCommand = this.repo.create(command);
            }
            if (newCommand != null) {
                this.show(user, exchange, newCommand.getId());
            }
            else {
                exchange.sendResponseHeaders(503, -1);
                exchange.close();
            }
        }
        else {
            exchange.sendResponseHeaders(400, -1);
            exchange.close();
        }

    }

    @Override
    public void update(User user, HttpExchange exchange, long id, IJsonObject payload) throws IOException {
        exchange.sendResponseHeaders(405, -1);
        exchange.close();
    }

    @Override
    public void delete(User user, HttpExchange exchange, long id) throws IOException {

        if (user.isAdmin() && this.repo.delete(id)) {
            IJsonObject jsonCommand = new JsonObjectService();
            jsonCommand.put("id", id);
            jsonCommand.put("deleted", true);
            String responseBody = jsonCommand.toJSONString();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseBody.getBytes().length);

            OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
            writer.write(jsonCommand.toJSONString());
            writer.flush();
            exchange.close();
        }
        else {
            exchange.sendResponseHeaders(503, -1);
            exchange.close();
        }

    }
}
