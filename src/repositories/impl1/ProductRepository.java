package repositories.impl1;

import contracts.ILogger;
import contracts.IRepository;
import entities.Product;
import entities.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements IRepository<Product> {
    private User user;
    private final DataSource dataSource;
    private final ILogger logger;

    public ProductRepository(DataSource dataSource, ILogger logger) {
        this.dataSource = dataSource;
        this.user = new User(0,"");
        this.logger = logger;
    }
    @Override
    public List<Product> all(long limit) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement st = allStatement(limit, this.user, connection);
            ResultSet rs = st.executeQuery();
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(
                    new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("unit_price"),
                        rs.getInt("tax_percent"),
                        rs.getString("image"),
                        rs.getDate("available_date"),
                        rs.getBoolean("in_shopping_card")
                ));
            }
            connection.close();
            return products;

        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException indexing all products: " +
                            e.getMessage()
            );
            return null;
        }

    }

    private PreparedStatement allStatement(long limit, User connectedUser, Connection connection) throws SQLException {
        boolean isAdmin = connectedUser.isAdmin();
        String sqlQuery = ProductSql.all(limit, isAdmin);
        PreparedStatement st = connection.prepareStatement(sqlQuery);
        if (!isAdmin ) {
            st.setLong(1, connectedUser.getId());
            if (limit > 0) {
                st.setLong(2, limit);
            }
        }
        else {
            if (limit > 0) {
                st.setLong(1, limit);
            }
        }
        return st;
    }

    @Override
    public Product get(long id) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement st = this.getStatement(id, connection);
            ResultSet rs = st.executeQuery();
            Product product;
            if (rs.next()) {
                connection.close();
                product = new Product(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getBigDecimal("unit_price"),
                    rs.getInt("tax_percent"),
                    rs.getString("image"),
                    rs.getDate("available_date")
                );
            }
            else {
                connection.close();
                return null;
            }
            return product;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException getting the product id : " + id +
                            e.getMessage()
            );
            return null;
        }
    }
    private PreparedStatement getStatement(long id, Connection connection) throws SQLException {
        String sqlQuery = ProductSql.get();
        PreparedStatement st = connection.prepareStatement(sqlQuery);
        st.setLong(1, id);
        return st;
    }

    @Override
    public Product create(Product product) {
        return null;
    }
    @Override
    public boolean update(long id, Product model) {
        return false;
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
