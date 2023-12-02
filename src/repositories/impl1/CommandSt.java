package repositories.impl1;

import entities.Command;
import entities.CommandLine;
import entities.User;

import java.sql.*;
import java.util.List;

public class CommandSt {
    public static PreparedStatement allStatement(long limit, User user, Connection connection) throws SQLException {
        String sqlQuery = CommandSql.all(limit);
        PreparedStatement st = connection.prepareStatement(sqlQuery);
        if (limit > 0) {
            st.setLong(1, limit);
            st.setLong(2, user.getId());
        }
        else {
            st.setLong(1, user.getId());
        }
        return st;
    }

    public static PreparedStatement getStatement(long id, User user, Connection connection) throws SQLException {
        String sqlQuery = CommandSql.get();
        PreparedStatement st = connection.prepareStatement(sqlQuery);
        st.setLong(1, id);
        st.setLong(2, user.getId());
        return st;
    }
    public static PreparedStatement createCommandStatement(User user, Command command, Connection connection) throws SQLException {
        String sqlQuery = CommandSql.create();
        PreparedStatement st = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
        st.setDate(1,  new Date(command.getCommandDate().getTime()));
        st.setLong(2, user.getId());
        st.setString(3, command.getDeliveryAddress().getTown());
        st.setString(4, command.getDeliveryAddress().getRegion());
        st.setString(5, command.getDeliveryAddress().getState());
        st.setString(6, command.getDeliveryAddress().getPostalCode());
        st.setString(7, command.getDeliveryAddress().getAddress());
        st.setString(8, command.getInvoiceAddress().getTown());
        st.setString(9, command.getInvoiceAddress().getRegion());
        st.setString(10, command.getInvoiceAddress().getState());
        st.setString(11, command.getInvoiceAddress().getPostalCode());
        st.setString(12, command.getInvoiceAddress().getAddress());
        st.setDate(13,  new Date(command.getDeliveryDate().getTime()));
        st.setString(14, command.getName());
        return st;
    }
    public static PreparedStatement createCommandLineStatement(Command command, Connection connection) throws SQLException  {
        String sqlQuery = CommandSql.createLine();
        PreparedStatement st = connection.prepareStatement(sqlQuery);
        List<CommandLine> lines = command.getLines();

        for (CommandLine line : lines) {
            PreparedStatement productSt = CommandSt.getCommandLineProductStatement(line.getProduct().getId(), connection);
            ResultSet rs = productSt.executeQuery();
            if (rs.next()) {
                st.setLong(1, line.getProduct().getId());
                st.setLong(2, command.getId());
                st.setBigDecimal(3, rs.getBigDecimal("unit_price"));
                st.setInt(4, rs.getInt("tax_percent"));
                st.setInt(5, line.getQty());
                st.addBatch();
            }
            else {
                connection.rollback();
                return null;
            }
        }
        return st;
    }
    public static PreparedStatement getCommandLineProductStatement(long productId, Connection connection) throws  SQLException {
        String sqlString = CommandSql.getProduct();
        PreparedStatement st = connection.prepareStatement(sqlString);
        st.setLong(1, productId);
        return st;
    }
    public static PreparedStatement deleteCommand(long commandId, Connection connection) throws  SQLException {
        String sqlString = CommandSql.deleteCommand();
        PreparedStatement st = connection.prepareStatement(sqlString);
        st.setLong(1, commandId);
        return st;
    }
    public static PreparedStatement deleteShoppingCard(User user, Connection connection) throws  SQLException {
        String sqlString = CommandSql.deleteShoppingCard();
        PreparedStatement st = connection.prepareStatement(sqlString);
        st.setLong(1, user.getId());
        return st;
    }
}
