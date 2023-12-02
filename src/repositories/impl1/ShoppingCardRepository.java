package repositories.impl1;

import contracts.ILogger;
import contracts.IRepository;
import entities.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCardRepository implements IRepository<ShoppingCard> {

    private User user;
    private final DataSource dataSource;
    private final ILogger logger;

    public ShoppingCardRepository(DataSource dataSource, ILogger logger) {
        this.dataSource = dataSource;
        this.user = new User(0,"");
        this.logger = logger;
    }
    @Override
    public List<ShoppingCard> all(long limit) {
        Connection connection;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement st = allStatement(limit, connection);
            ResultSet rs = st.executeQuery();
            List<ShoppingCard> shoppingCard = new ArrayList<>();
            while (rs.next()) {
                shoppingCard.add(
                        new ShoppingCard(
                                rs.getLong("id"),
                                this.user,
                                new Product(
                                    rs.getLong("product_id"),
                                    rs.getString("product_name"),
                                        rs.getBigDecimal("unit_price"),
                                        rs.getInt("tax_percent"),
                                        rs.getString("image"),
                                        rs.getDate("available_date")
                                ),
                                rs.getInt("qty")
                        ));
            }
            connection.close();
            return shoppingCard;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException indexing all shopping card items: " +
                            e.getMessage()
            );
            return null;
        }

    }

    private PreparedStatement allStatement(long limit, Connection connection) throws SQLException {
        String sqlQuery = ShoppingCardSql.all(limit);
        PreparedStatement st = connection.prepareStatement(sqlQuery);
        st.setLong(1, this.user.getId());
        if (limit > 0) {
            st.setLong(2, limit);
        }
        return st;
    }

    @Override
    public ShoppingCard get(long id) {
        Connection connection;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement st = this.getStatement(id, connection);
            ResultSet rs = st.executeQuery();
            ShoppingCard shoppingCard = null;
            if (rs.next()) {
                shoppingCard = new ShoppingCard(
                        rs.getLong("id"),
                        this.user,
                        new Product(
                                rs.getLong("product_id"),
                                rs.getString("product_name"),
                                rs.getBigDecimal("unit_price"),
                                rs.getInt("tax_percent"),
                                rs.getString("image"),
                                rs.getDate("available_date")
                        ),
                        rs.getInt("qty")
                );
            }
            connection.close();
            return shoppingCard;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException getting shopping card item of id " + id + " : " +
                            e.getMessage()
            );
            return null;
        }
    }
    private PreparedStatement getStatement(long id, Connection connection) throws SQLException {
        String sqlQuery = ShoppingCardSql.get();
        PreparedStatement st = connection.prepareStatement(sqlQuery);
        st.setLong(1, id);
        st.setLong(2, this.user.getId());
        return st;
    }

    @Override
    public ShoppingCard create(ShoppingCard shoppingCard) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement createSt = ShoppingCardSt.createStatement(shoppingCard, this.user, connection);
            createSt.executeUpdate();
            ResultSet ids = createSt.getGeneratedKeys();
            if (ids.next()) {
                shoppingCard.setId(ids.getLong(1));
                connection.close();
                return shoppingCard;
            }
            connection.close();
            return null;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException creating shopping card item: " +
                            e.getMessage()
            );
            return null;
        }
    }

    @Override
    public boolean update(long id, ShoppingCard model) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(ShoppingCardSql.update());
            st.setInt(1,model.getQty());
            st.setLong(2, id);
            st.setLong(3, this.user.getId());
            return st.executeUpdate() > 0;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException updating shopping card of id " + id + " : " +
                            e.getMessage()
            );
            return false;
        }
    }

    @Override
    public boolean delete(long id) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(ShoppingCardSql.delete());
            st.setLong(1, id);
            st.setLong(2, this.user.getId());
            return st.executeUpdate() > 0;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException deleting shopping card item with id : " + id + " : " +
                            e.getMessage()
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
