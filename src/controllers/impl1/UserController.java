package controllers.impl1;

import com.sun.net.httpserver.HttpExchange;
import contracts.*;
import entities.User;
import services.impl1.JSON.JsonArrayService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class UserController implements IHttpController {
    private final IRepository<User> repo;
    public UserController(IRepository<User> repo) {
        this.repo = repo;
    }
    @Override
    public void setRepositoryUser(User user) {
        this.repo.setUser(user);
    }
    @Override
    public void index(User user, HttpExchange exchange, long limit) throws IOException {
        if (user.isAdmin()) {
            List<User> users = this.repo.all(limit);
            IJsonArray jsonUsers = new JsonArrayService();
            for (User user1 : users) {
                IJsonObject json = user1.toJSONObject();
                jsonUsers.add(json);
            }
            String responseBody = jsonUsers.toJSONString();
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, responseBody.getBytes().length);
            OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
            writer.write(responseBody);
            writer.flush();
            exchange.close();
        }
        else
        {
            exchange.sendResponseHeaders(403, -1);
            exchange.close();
        }
    }

    @Override
    public void show(User user, HttpExchange exchange, long id) throws IOException {

        IJsonObject jsonUser = user.toJSONObject();
        String responseBody = jsonUser.toJSONString();
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, responseBody.getBytes().length);
        OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
        writer.write(responseBody);
        writer.flush();
        exchange.close();
    }
    @Override
    public void create(User user, HttpExchange exchange, IJsonObject payload) throws IOException {
        if (user.isAdmin()) {
            User user1 = User.fromJSONObject(payload);
            if (user1 != null) {
                User newUser = this.repo.create(user1);
                if (newUser != null) {
                    this.show(user, exchange, newUser.getId());
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
        else
        {
            exchange.sendResponseHeaders(403, -1);
            exchange.close();
        }
    }

    @Override
    public void update(User user, HttpExchange exchange, long id, IJsonObject payload) throws IOException {
        User user1 = User.fromJSONObject(payload);
        if (user1 != null) {
            if (this.repo.update(id, user1)) {
                this.show(user, exchange, id);
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
    public void delete(User user, HttpExchange exchange, long id) throws IOException {
        exchange.sendResponseHeaders(405, -1);
        exchange.close();
    }

}
