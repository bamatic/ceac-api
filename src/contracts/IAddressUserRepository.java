package contracts;

import entities.Address;
import entities.User;
/**
 * Use this repository to create addresses and assign then to the connected user
 *
 */
public interface IAddressUserRepository {
    /**
     * Create an address an assign it to the connected user
     *
     * @param address data for create an address
     * @param type delivery or billing address
     * @return the created user or null
     */
    Address createForUser(Address address, String type);
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
