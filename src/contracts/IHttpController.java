package contracts;

import com.sun.net.httpserver.HttpExchange;
import entities.User;


import java.io.IOException;


/**
 * For each registered route an IHttpController MUST BE implemented
 * the implemented controller has to access to the needed repositories
 */
public interface IHttpController {
    /**
     * if limit is 0 all models of the controller repository
     * or a limit number of models if limit > 0.
     *
     * @param user the connected user
     * @param exchange the Http exchange
     * @param limit max number of models or all if limit = 0
     */
    void index(User user, HttpExchange exchange, long limit) throws IOException;
    /**
     * get the model identified by id from the controller's repository
     *
     * @param user the connected user
     * @param exchange the Http exchange
     * @param id id of the model to get
     */
    void show(User user, HttpExchange exchange, long id) throws IOException;
    /**
     * create a new model in the controller's repository
     *
     * @param user the connected user
     * @param exchange the Http exchange
     * @param payload json object
     */
    void create(User user, HttpExchange exchange, IJsonObject payload) throws IOException;
    /**
     * update the model identified by id in the controller's repository
     * with the fields of the json object in the payload
     *
     * @param user the connected user
     * @param exchange the Http exchange
     * @param id id of the model to update
     * @param payload json object
     */
    void update(User user, HttpExchange exchange, long id, IJsonObject payload) throws IOException;
    /**
     * delete the model identified by id in the controller's repository
     *
     * @param user the connected user
     * @param exchange the Http exchange
     * @param id id of the model to update
     */
    void delete(User user, HttpExchange exchange, long id) throws IOException;
    /**
     * set the repository user to the given user
     *
     * @param user the connected user
     */
    void setRepositoryUser(User user);
}
