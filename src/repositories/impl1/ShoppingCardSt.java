package repositories.impl1;

import entities.ShoppingCard;
import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ShoppingCardSt {
    public static PreparedStatement createStatement(ShoppingCard shoppingCard, User connectedUser, Connection connection) throws SQLException {
        String sqlQuery = ShoppingCardSql.create();
        PreparedStatement st = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
        st.setLong(1, shoppingCard.getProduct().getId());
        st.setLong(2, connectedUser.getId());
        st.setInt(3, shoppingCard.getQty());
        return st;
    }
}
