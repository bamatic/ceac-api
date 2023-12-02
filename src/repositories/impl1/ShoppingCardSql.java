package repositories.impl1;

public class ShoppingCardSql {
    public static String get() {
        return "SELECT " +
                "sc.id as id, sc.qty, " +
                "p.id as product_id, p.name as product_name, p.available_date, p.image, p.unit_price, p.tax_percent " +
                "FROM shopping_card sc " +
                "INNER JOIN products p ON sc.product_id = p.id " +
                "WHERE sc.id=? and user_id=?" ;
}
    public static String all(long limit) {
        String query = "SELECT " +
                "sc.id as id, sc.qty, " +
                "p.id as product_id, p.name as product_name, p.available_date, p.image, p.unit_price, p.tax_percent " +
                "FROM shopping_card sc " +
                "INNER JOIN products p ON sc.product_id = p.id " +
                "WHERE sc.user_id=? " +
                "ORDER BY id DESC";

        if (limit > 0) {
            query += "LIMIT ?";
        }
        return query;
    }
    public static String create() {
        return "INSERT INTO shopping_card " +
                "(product_id, user_id, qty)" +
                " VALUES (?,?,?)";
    }
    public static String update() {
        return "UPDATE shopping_card set qty=? WHERE id=? and user_id=?";
    }
    public static String delete() {
        return "DELETE FROM shopping_card WHERE id=? AND user_id=?";
    }
}
