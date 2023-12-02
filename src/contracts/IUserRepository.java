package contracts;

import entities.User;

import java.util.List;

public interface IUserRepository {
    User getUserAndPassword(String email, String password);
}
