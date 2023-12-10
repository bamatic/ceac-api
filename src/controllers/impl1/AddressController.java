package controllers.impl1;

import com.sun.net.httpserver.HttpExchange;
import contracts.*;
import entities.Address;
import entities.User;
import http.HttpRequestManager;
import services.impl1.JSON.JsonArrayService;
import services.impl1.JSON.JsonObjectService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class AddressController implements IHttpController {

    private final IRepository<Address> repo;
    private final IAddressUserRepository addressUserRepo;
    private final ILogger logger;
    public AddressController(IRepository<Address> repo, IAddressUserRepository addressUserRepo, ILogger logger ) {
        this.repo = repo;
        this.addressUserRepo = addressUserRepo;
        this.logger = logger;
    }
    @Override
    public void setRepositoryUser(User user) {
        this.repo.setUser(user);
        this.addressUserRepo.setUser(user);
    }
    @Override
    public void index(User user, HttpExchange exchange, long limit) throws IOException {
        List<Address> addresses = this.repo.all(limit);
        IJsonArray jsonAddresses = new JsonArrayService();
        for (Address address : addresses) {
            IJsonObject json = address.toJSONObject();
            jsonAddresses.add(json);
        }
        String responseBody = jsonAddresses.toJSONString();
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, responseBody.getBytes().length);
        OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
        writer.write(responseBody);
        writer.flush();
        exchange.close();
    }

    @Override
    public void show(User user, HttpExchange exchange, long id) throws IOException {
        Address address = this.repo.get(id);
        if (address == null) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
        }
        else {
            IJsonObject jsonAddresses = address.toJSONObject();
            String responseBody = jsonAddresses.toJSONString();
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
        Address address = Address.fromJSONObject(payload);
        if (address != null) {
            Address newAddress = null;
            HttpRequestManager requestManager = new HttpRequestManager(exchange, this.logger);
            if (requestManager.isDelivery() || requestManager.isInvoice()) {
                newAddress = this.addressUserRepo.createForUser(address, requestManager.getCustomerAddress());
            }
            else {
                newAddress = this.repo.create(address);
            }
            if (newAddress != null) {
                this.show(user, exchange, newAddress.getId());
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
        Address address = Address.fromJSONObject(payload);
        if (address != null) {
            if (this.repo.update(id, address)) {
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
        if (this.repo.delete(id)) {
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
