package contracts;

import entities.Address;
import entities.User;

public interface IAddressUserRepository {
    Address createForUser(Address address, String type);
    User getUser();
    void setUser(User user);
}
