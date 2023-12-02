package controllers;

import com.sun.net.httpserver.HttpExchange;
import contracts.*;
import entities.Product;
import entities.User;
import services.impl1.JSON.JsonArrayService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class ProductController implements IHttpController {

    private final IRepository<Product> repo;
    public ProductController(IRepository<Product> repo) {
        this.repo = repo;
    }
    @Override
    public void setRepositoryUser(User user) {
        this.repo.setUser(user);
    }
    @Override
    public void index(User user, HttpExchange exchange, long limit) throws IOException {
        List<Product> products = this.repo.all(limit);
        IJsonArray jsonProducts = new JsonArrayService();
        for (Product product : products) {
            IJsonObject json = product.toJSONObject();
            jsonProducts.add(json);
        }
        String responseBody = jsonProducts.toJSONString();
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, responseBody.getBytes().length);
        OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
        writer.write(responseBody);
        writer.flush();
        exchange.close();
    }

    @Override
    public void show(User user, HttpExchange exchange, long id) throws IOException {
        Product product = this.repo.get(id);
        if (product == null) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
        }
        else {
            IJsonObject jsonCommand = product.toJSONObject();
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
