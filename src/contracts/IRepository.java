package contracts;

import entities.User;

import java.util.List;

/**
 * an Interface to be implemented
 * by repository classes
 * access your data using repositories
 *
 *
 */
public interface IRepository<T> {
    /**
     * get a maximum of <T> objects from the repository
     * or all if limit = 0
     *
     * @param limit a positive integer to set the max size of the returned list or 0 to get all objects
     * @return  a List of <T> objects
     *
     */
    List<T> all(long limit);
    /**
     * get the  <T> object identified by id
     *
     *
     * @param id the <T> object identifier
     * @return  a  <T> object or null
     *
     */
    T get(long id);
    /**
     * creates a new  <T> object
     *
     *
     * @param model data to create the new <T> object
     * @return  a new <T> object or null
     *
     */
    T create(T model);
    /**
     *  updates the <T> object identified by id
     *
     *
     * @param id the <T> object identifier
     * @return  true if the object is updated
     *
     */
    boolean update(long id, T model);
    /**
     * delete the  <T> object identified by id
     *
     *
     * @param id the <T> object identifier
     * @return  true if the object is deleted
     *
     */
    boolean delete(long id);
    /**
     *
     *
     * @return  the connected user
     *
     */
    User getUser();
    /**
     *
     *
     * @param  user set the connected user
     *
     */
    void setUser(User user);
}
