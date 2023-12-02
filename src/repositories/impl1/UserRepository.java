package repositories.impl1;

import contracts.ILogger;
import contracts.IRepository;
import contracts.IUserRepository;
import entities.Address;
import entities.User;
import http.HttpRequestManager;

import javax.sql.DataSource;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IRepository<User> {
    private final DataSource dataSource;
    private User user;
    private final ILogger logger;

    public UserRepository(DataSource dataSource, ILogger logger) {
        this.dataSource = dataSource;
        this.user = new User(0,"");
        this.logger = logger;
    }
    @Override
    public List<User> all(long limit) {
        if (!this.user.getRole().equals("admin")) {
            return null;
        }
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement st = UserSt.all(limit, connection);
            ResultSet rs = st.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(
                       new User(
                                rs.getLong("id"),
                                rs.getString("tax_id"),
                                rs.getString("name"),
                               rs.getString("surname"),
                                new Address(
                                        rs.getLong("delivery_id"),
                                        rs.getString("delivery_town"),
                                        rs.getString("delivery_region"),
                                        rs.getString("delivery_state"),
                                        rs.getString("delivery_postal_code"),
                                        rs.getString("delivery_address")
                                ),
                                new Address(
                                        rs.getLong("invoice_id"),
                                        rs.getString("invoice_town"),
                                        rs.getString("invoice_region"),
                                        rs.getString("invoice_state"),
                                        rs.getString("invoice_postal_code"),
                                        rs.getString("invoice_address")
                                ),
                                rs.getString("email"),
                                rs.getString("role")
                       )
                );
            }
            connection.close();
            return users;

        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException while getting all users: " + e.getMessage()
            );
            return null;
        }
    }

    @Override
    public User get(long id) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement st = UserSt.get(id, this.user, connection);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                User u = new User(
                        rs.getLong("id"),
                        rs.getString("tax_id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        new Address(
                                rs.getLong("delivery_id"),
                                rs.getString("delivery_town"),
                                rs.getString("delivery_region"),
                                rs.getString("delivery_state"),
                                rs.getString("delivery_postal_code"),
                                rs.getString("delivery_address")
                        ),
                        new Address(
                                rs.getLong("invoice_id"),
                                rs.getString("invoice_town"),
                                rs.getString("invoice_region"),
                                rs.getString("invoice_state"),
                                rs.getString("invoice_postal_code"),
                                rs.getString("invoice_address")
                        ),
                        rs.getString("email"),
                        rs.getString("role")

                );
                connection.close();
                return u;
            }
            connection.close();
            return null;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException while getting a user by id: " + e.getMessage()
            );
            return null;
        }

    }

    @Override
    public User create(User model) {
        if (this.user.isAdmin()) {
            Connection connection = null;
            try {
                connection = this.dataSource.getConnection();
                PreparedStatement createSt = UserSt.create(user, connection);
                createSt.executeUpdate();
                ResultSet ids = createSt.getGeneratedKeys();
                if (ids.next()) {
                    user.setId(ids.getLong(1));
                    connection.close();
                    return user;
                }

                connection.close();
                return null;
            } catch (SQLException e) {
                this.logger.message(
                        "SEVERE",
                        "SQLException creating a user: " + e.getMessage()
                );
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean update(long id, User model) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement updateSt;
            if (model.getStrPassword() != null) {
                updateSt = UserSt.updateEmailPassword(id, model, this.user, connection);
            }
            else {
                updateSt = UserSt.updateEmail(id, model, this.user, connection);
            }
            updateSt.executeUpdate();
            connection.close();

            return true;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException updating a user: " + e.getMessage()
            );
            return false;
        }
        catch (NoSuchAlgorithmException e) {
            this.logger.message(
                    "SEVERE",
                    "NoSuchAlgorithmException while updating a user: " + e.getMessage()
            );
            return false;
        }
        catch (InvalidKeySpecException e) {
            this.logger.message(
                    "SEVERE",
                    "InvalidKeySpecException while updating a user: " + e.getMessage()
            );
            return false;
        }
    }

    @Override
    public boolean delete(long id) {
        return false;
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
