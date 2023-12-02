package contracts;

import entities.User;

import java.util.List;

public interface IRepository<T> {
    List<T> all(long limit);
    T get(long id);
    T create(T model);
    boolean update(long id, T model);
    boolean delete(long id);
    User getUser();
    void setUser(User user);
}
