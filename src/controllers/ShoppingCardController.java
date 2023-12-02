package controllers;

import com.sun.net.httpserver.HttpExchange;
import contracts.*;
import entities.ShoppingCard;
import entities.User;
import services.impl1.JSON.JsonArrayService;
import services.impl1.JSON.JsonObjectService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class ShoppingCardController implements IHttpController {

    private final IRepository<ShoppingCard> repo;
    public ShoppingCardController(IRepository<ShoppingCard> repo) {
        this.repo = repo;
    }
    @Override
    public void setRepositoryUser(User user) {
        this.repo.setUser(user);
    }
    @Override
    public void index(User user, HttpExchange exchange, long limit) throws IOException {
        List<ShoppingCard> shoppingCards = this.repo.all(limit);
        IJsonArray jsonItems = new JsonArrayService();
        for (ShoppingCard jsomItem : shoppingCards) {
            IJsonObject json = jsomItem.toJSONObject();
            jsonItems.add(json);
        }
        String responseBody = jsonItems.toJSONString();
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, responseBody.getBytes().length);
        OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
        writer.write(responseBody);
        writer.flush();
        exchange.close();
    }

    @Override
    public void show(User user, HttpExchange exchange, long id) throws IOException {
        ShoppingCard shoppingCard = this.repo.get(id);
        if (shoppingCard == null) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
        }
        else {
            IJsonObject jsonShoppingCard = shoppingCard.toJSONObject();
            String responseBody = jsonShoppingCard.toJSONString();
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
        ShoppingCard shoppingCard = ShoppingCard.fromJSONObject(user, payload);
        if (shoppingCard != null) {
            ShoppingCard newShoppingCard = this.repo.create(shoppingCard);
            if (newShoppingCard != null) {
                this.show(user, exchange, newShoppingCard.getId());
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
        ShoppingCard shoppingCard = ShoppingCard.fromJSONObject(user, payload);
        if (shoppingCard != null) {
            if (this.repo.update(id, shoppingCard)) {
                this.show(user, exchange, id);
                exchange.close();
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
        if (this.repo.delete(id)) {
            IJsonObject jsonShoppingCard = new JsonObjectService();
            jsonShoppingCard.put("id", id);
            jsonShoppingCard.put("deleted", true);
            String responseBody = jsonShoppingCard.toJSONString();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseBody.getBytes().length);

            OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
            writer.write(jsonShoppingCard.toJSONString());
            writer.flush();
            exchange.close();
        }
        else {
            exchange.sendResponseHeaders(503, -1);
            exchange.close();
        }
    }
}
