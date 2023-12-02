package repositories.impl1;

public class ProductSql {
    public static String all(long limit, boolean isAdmin) {
        String sqlString;
        if (isAdmin) {
            sqlString = "SELECT " +
                    "p.id, p.name, p.unit_price, p.tax_percent, p.image, p.available_date " +
                    "FROM products p";
        }
        else {
            sqlString = "SELECT " +
                    "p.id, p.name, p.unit_price, p.tax_percent, p.image, p.available_date, sp.id is not null as in_shopping_card " +
                    "FROM products p " +
                    "LEFT JOIN shopping_card sp ON sp.product_id = p.id AND sp.user_id=?";
        }
        if (limit > 0) {
            sqlString += " LIMIT ?";
        }
        return sqlString;
    }
    public static String get() {
        return  "SELECT " +
                "id, name, unit_price, tax_percent, image, available_date " +
                "FROM products " +
                "WHERE id=?";
    }
}
