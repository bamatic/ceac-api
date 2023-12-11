package contracts;

import entities.User;

import java.util.List;
/**
 * access users using credentials
 *
 */
public interface IUserRepository {
    /**
     * get the user data using email and password
     *
     * @param email the user's email
     * @param password the password
     * @return  the authenticated user or null
     *
     */
    User getUserAndPassword(String email, String password);
}
