package repositories.impl1;

import entities.User;
import services.impl1.jwt.PasswordHashService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class UserSt {

    public static PreparedStatement all(long limit, Connection connection) throws SQLException {
        String strSql = UserSql.get();
        PreparedStatement st = connection.prepareStatement(strSql);
        if (limit > 0) {
            st.setLong(1, limit);
        }
        return st;
    }
    public static PreparedStatement get(long id, User connectedUser, Connection connection) throws SQLException {
        String strSql = UserSql.get();
        PreparedStatement st = connection.prepareStatement(strSql);
        if (connectedUser.isAdmin()) {
            st.setLong(1, id);
        }
        else {
            st.setLong(1, connectedUser.getId());
        }
        return st;
    }
    public static PreparedStatement create(User user, Connection connection) throws SQLException {
        String sqlString = UserSql.create();
        PreparedStatement st = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
        st.setString(1, user.getName());
        return st;
    }
    public static PreparedStatement updateEmailPassword(long id, User newUser, User connectedUser, Connection connection) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        String sqlString = UserSql.updateEmailPassword();
        PreparedStatement st = connection.prepareStatement(sqlString);
        st.setString(1, newUser.getEmail());
        st.setBytes(2, PasswordHashService.hashPassword(newUser.getStrPassword()));
        if (connectedUser.isAdmin()) {
            st.setLong(3, id);
        }
        else {
            st.setLong(3, connectedUser.getId());
        }
        return st;
    }
    public static PreparedStatement updateEmail(long id, User newUser, User connectedUser, Connection connection) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        String sqlString = UserSql.updateEmail();
        PreparedStatement st = connection.prepareStatement(sqlString);
        st.setString(1, newUser.getEmail());
        if (connectedUser.isAdmin()) {
            st.setLong(2, id);
        }
        else {
            st.setLong(2, connectedUser.getId());
        }
        return st;
    }
    public static PreparedStatement getUserAndPassword(String email, String password,  Connection connection) throws
            SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        String strSql = UserSql.getUserAndPassword();
        PreparedStatement st = connection.prepareStatement(strSql);
        st.setString(1, email);
        st.setBytes(2, PasswordHashService.hashPassword(password));
        return st;
    }
}
