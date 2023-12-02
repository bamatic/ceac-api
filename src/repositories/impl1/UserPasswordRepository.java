package repositories.impl1;

import contracts.ILogger;
import contracts.IUserRepository;
import entities.User;

import javax.sql.DataSource;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPasswordRepository implements IUserRepository {
    private final DataSource dataSource;
    private final ILogger logger;
    public UserPasswordRepository(DataSource dataSource, ILogger logger) {
        this.dataSource = dataSource;
        this.logger = logger;
    }
    public User getUserAndPassword(String email, String password) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement st = UserSt.getUserAndPassword(email, password, connection);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                connection.close();
                return new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getBytes("password")
                );
            }
            connection.close();
            return null;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException getting user by user and password " +
                            e.getMessage()
            );
            return null;
        }
        catch (InvalidKeySpecException e) {
            this.logger.message(
                    "SEVERE",
                    "InvalidKeySpecException getting user by user and password " +
                            e.getMessage()
            );
            return null;
        }
        catch (NoSuchAlgorithmException e) {
            this.logger.message(
                    "SEVERE",
                    "NoSuchAlgorithmException getting user by user and password " +
                            e.getMessage()
            );
            return null;
        }

    }
}
