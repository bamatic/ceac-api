package contracts;

import entities.Command;
import entities.User;

public interface ICommandFromShoppingCard {
    Command createSP(Command command);
    User getUser();
    void setUser(User user);
}
