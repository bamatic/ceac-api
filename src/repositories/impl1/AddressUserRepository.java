package repositories.impl1;

import contracts.IAddressUserRepository;
import contracts.ILogger;
import entities.Address;
import entities.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressUserRepository implements IAddressUserRepository {
    private User user;
    private final DataSource dataSource;
    private final ILogger logger;

    public AddressUserRepository(DataSource dataSource, ILogger logger) {
        this.dataSource = dataSource;
        this.user = new User(0,"");
        this.logger = logger;
    }
    @Override
    public Address createForUser(Address address, String type) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement createSt = AddressSt.createStatement(address, this.user, connection);
            createSt.executeUpdate();
            ResultSet ids = createSt.getGeneratedKeys();
            if (ids.next()) {
                address.setId(ids.getLong(1));
                PreparedStatement updateUser;
                if ( !type.equals("delivery") && !type.equals("invoice")) {
                    try {
                        connection.rollback();
                        connection.close();
                        return null;
                    } catch (SQLException e) {
                        connection.close();
                        this.logger.message(
                                "SEVERE",
                                "SQLException creating an address for connected user: " + e.getMessage()
                        );
                        throw new RuntimeException(e);
                    }
                }
                else {
                    if (type.equals("delivery")) {
                        updateUser = AddressSt.updateDelivery(address.getId(), this.user.getId(), connection);
                    }
                    else {
                        updateUser = AddressSt.updateInvoice(address.getId(), this.user.getId(), connection);
                    }
                    if (updateUser.executeUpdate() == 1) {
                        connection.commit();
                        connection.close();
                        return address;
                    }
                    else if (connection != null) {
                        try {
                            connection.rollback();
                            connection.close();
                            return null;
                        } catch (SQLException ex) {
                            this.logger.message(
                                    "SEVERE",
                                    "SQLException rolling back while creating an address for the connected user: " +
                                            ex.getMessage()
                            );
                            connection.close();
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }

            connection.setAutoCommit(true);
            connection.close();
            return null;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException creating an address for the connected user: " +
                            e.getMessage()
            );
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                    return null;
                } catch (SQLException ex) {
                    this.logger.message(
                            "SEVERE",
                            "SQLException rolling back while creating an address for the connected user: " +
                                    ex.getMessage()
                    );
                    throw new RuntimeException(ex);
                }
            }
            return null;
        }
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
