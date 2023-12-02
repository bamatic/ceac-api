package repositories.impl1;

import entities.Address;
import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class AddressSt {
    public static  PreparedStatement allStatement(long limit, User connectedUser, Connection connection) throws SQLException {
        String sqlQuery = AddressSql.all(limit);
        PreparedStatement st = connection.prepareStatement(sqlQuery);
        st.setLong(1, connectedUser.getId());
        if (limit > 0) {
            st.setLong(2, limit);
        }
        return st;
    }
    public static PreparedStatement getStatement(long id, User connectedUser, Connection connection) throws SQLException {
        String sqlQuery = AddressSql.get();
        PreparedStatement st = connection.prepareStatement(sqlQuery);
        st.setLong(1, id);
        st.setLong(2, connectedUser.getId());
        return st;
    }
    public static PreparedStatement createStatement(Address address, User connectedUser, Connection connection) throws SQLException {
        String stringQuery = AddressSql.create();
        PreparedStatement st = connection.prepareStatement(stringQuery, Statement.RETURN_GENERATED_KEYS);
        st.setString(1, address.getTown());
        st.setString(2, address.getRegion());
        st.setString(3, address.getState());
        st.setString(4, address.getAddress());
        st.setString(5, address.getPostalCode());
        st.setLong(6, connectedUser.getId());
        return st;
    }
    public static PreparedStatement updateStatement(long id, Address address, User connectedUser, Connection connection) throws SQLException {
        String stringQuery = AddressSql.update();
        PreparedStatement st = connection.prepareStatement(stringQuery, Statement.RETURN_GENERATED_KEYS);
        st.setString(1, address.getTown());
        st.setString(2, address.getRegion());
        st.setString(3, address.getState());
        st.setString(4, address.getAddress());
        st.setString(5, address.getPostalCode());
        st.setLong(6, id);
        st.setLong(7, connectedUser.getId());
        return st;
    }
    public static PreparedStatement updateDelivery(long deliveryId, long userId, Connection connection) throws SQLException {
        String stringQuery = AddressSql.updateDelivery();
        PreparedStatement st = connection.prepareStatement(stringQuery, Statement.RETURN_GENERATED_KEYS);
        st.setLong(1, deliveryId);
        st.setLong(2, userId);
        return st;
    }
    public static PreparedStatement updateInvoice(long deliveryId, long userId, Connection connection) throws SQLException {
        String stringQuery = AddressSql.invoiceDelivery();
        PreparedStatement st = connection.prepareStatement(stringQuery, Statement.RETURN_GENERATED_KEYS);
        st.setLong(1, deliveryId);
        st.setLong(2, userId);
        return st;
    }
}
