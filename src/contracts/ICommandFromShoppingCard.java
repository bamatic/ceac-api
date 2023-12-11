package contracts;

import entities.Command;
import entities.User;

/**
 * Use this repository to create a command from the shopping card
 *
 */
public interface ICommandFromShoppingCard {
    /**
     * Create a command and empty the shopping card
     *
     * @param command data for create a command
     * @return the created command or null
     */
    Command createSP(Command command);
    /**
     * Create an address an assign it to the connected user
     *
     * @return  the connected user
     *
     */
    User getUser();
    /**
     * Create an address an assign it to the connected user
     *
     * @param  user set the connected user
     *
     */
    void setUser(User user);
}
