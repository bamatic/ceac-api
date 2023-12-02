package repositories.impl1;

import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddressSql {
    public static String all(long limit) {
        String sqlString = "SELECT " +
                "id, town, region, state, address, postal_code " +
                "FROM addresses " +
                "WHERE user_id=?";
        if (limit > 0) {
            sqlString += " LIMIT ?";
        }
        return sqlString;
    }
    public static String get() {
        return  "SELECT " +
                "id, user_id, town, region, state, address, postal_code " +
                "FROM addresses " +
                "WHERE id=? AND user_id=?";
    }
    public static String create() {
        return "INSERT INTO addresses (town, region, state, address, postal_code, user_id) " +
                "VALUES (?,?,?,?,?,?)";
    }
    public static String update() {
        return "UPDATE addresses set town=?, region=?, state=?, address=?, postal_code=? " +
                "WHERE id=? AND user_id=?";
    }
    public static String delete() {
        return "DELETE FROM addresses WHERE id=? AND user_id=?";
    }
    public static String updateDelivery() {
        return "UPDATE users set delivery_address_id=? WHERE id=?";
    }
    public static String invoiceDelivery() {
        return "UPDATE users set invoice_address_id=? WHERE id=?";
    }
}
