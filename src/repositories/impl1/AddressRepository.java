package repositories.impl1;

import contracts.ILogger;
import contracts.IRepository;
import entities.Address;
import entities.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressRepository implements IRepository<Address> {
    private User user;
    private final DataSource dataSource;
    private final ILogger logger;

    public AddressRepository(DataSource dataSource, ILogger logger) {
        this.dataSource = dataSource;
        this.user = new User(0,"");
        this.logger = logger;
    }
    @Override
    public List<Address> all(long limit) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement st = AddressSt.allStatement(limit, this.user, connection);
            ResultSet rs = st.executeQuery();
            List<Address> addresses = new ArrayList<>();
            while (rs.next()) {
                addresses.add(
                    new Address(
                        rs.getLong("id"),
                        rs.getString("town"),
                        rs.getString("region"),
                        rs.getString("state"),
                        rs.getString("address"),
                        rs.getString("postal_code")
                ));
            }
            connection.close();
            return addresses;

        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException getting all addresses: " + e.getMessage()
            );
            return null;
        }

    }
    @Override
    public Address get(long id) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement st = AddressSt.getStatement(id, this.user, connection);
            ResultSet rs = st.executeQuery();
            Address address;
            if (rs.next()) {
                address = new Address(
                        rs.getLong("id"),
                        rs.getString("town"),
                        rs.getString("region"),
                        rs.getString("state"),
                        rs.getString("address"),
                        rs.getString("postal_code")
                );
            }
            else {
                return null;
            }
            connection.close();
            return address;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException getting an address: " + e.getMessage()
            );
            return null;
        }
    }
    @Override
    public Address create(Address address) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement createSt = AddressSt.createStatement(address, this.user, connection);
            createSt.executeUpdate();
            ResultSet ids = createSt.getGeneratedKeys();
            if (ids.next()) {
                address.setId(ids.getLong(1));
                return address;
            }

            connection.close();
            return null;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException creating an address: " + e.getMessage()
            );
            return null;
        }
    }
    @Override
    public boolean update(long id, Address address) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement updateSt = AddressSt.updateStatement(id, address, this.user, connection);
            updateSt.executeUpdate();
            connection.close();

            return true;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException updating an address: " + e.getMessage()
            );
            return false;
        }
    }

    @Override
    public boolean delete(long id) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(AddressSql.delete());
            st.setLong(1, id);
            st.setLong(2, this.user.getId());
            connection.close();
            return st.executeUpdate() > 0;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException deleting an address: " + e.getMessage()
            );
            return false;
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
