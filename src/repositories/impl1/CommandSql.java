package repositories.impl1;

public class CommandSql {
    public static String get() {
        return "SELECT " +
                "c.id as id, c.name as command_name, c.command_date, c.delivery_date, c.state, " +
                "c.delivery_address, c.delivery_postal_code, c.delivery_town, c.delivery_region, c.delivery_state, " +
                "c.invoice_address, c.invoice_postal_code, c.invoice_town, c.invoice_region, c.invoice_state, " +
                "cl.id as command_line_id, cl.tax_percent, cl.unit_price, cl.qty, " +
                "p.id as product_id, p.name, p.available_date, p.image " +
                "FROM commands c " +
                "INNER JOIN command_lines cl ON cl.command_id = c.id " +
                "INNER JOIN products p ON cl.product_id = p.id " +
                "WHERE c.id=? and user_id=?" ;
    }
    public static String all(long limit) {
        if (limit  <= 0) {
            return "SELECT " +
                    "c.id as id, c.name as command_name, c.command_date, c.delivery_date, c.state, " +
                    "c.delivery_address, c.delivery_postal_code, c.delivery_town, c.delivery_region, c.delivery_state, " +
                    "c.invoice_address, c.invoice_postal_code, c.invoice_town, c.invoice_region, c.invoice_state, " +
                    "cl.id as command_line_id, cl.tax_percent, cl.unit_price, cl.qty, " +
                    "p.id as product_id, p.name, p.available_date, p.image " +
                    "FROM commands c " +
                    "INNER JOIN command_lines cl ON cl.command_id = c.id " +
                    "INNER JOIN products p ON cl.product_id = p.id " +
                    "WHERE c.user_id=? " +
                    "ORDER BY id DESC ";
        }
        return "WITH last_commands AS (SELECT \n" +
                "    id,\n" +
                "    user_id,\n" +
                "    name as command_name,\n" +
                "    command_date,\n" +
                "    delivery_date,\n" +
                "    state " +
                "FROM commands ORDER BY id DESC LIMIT ?) " +
                "SELECT " +
                "c.id as id, c.command_name, c.command_date, c.delivery_date, c.state, " +
                "cl.id as command_line_id, cl.tax_percent, cl.unit_price, cl.qty, " +
                "p.id as product_id, p.name, p.available_date, p.image " +
                "FROM last_commands c " +
                "INNER JOIN command_lines cl ON cl.command_id = c.id " +
                "INNER JOIN products p ON cl.product_id = p.id " +
                "WHERE c.user_id=? " +
                "ORDER BY id DESC ";
    }
    public static String create() {
        return "INSERT INTO commands (" +
                "command_date, user_id, " +
                "delivery_town,delivery_region, delivery_state, delivery_postal_code, delivery_address," +
                "invoice_town,invoice_region, invoice_state, invoice_postal_code, invoice_address," +
                "delivery_date, name" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
    public static String createLine() {
        return "INSERT INTO command_lines (product_id, command_id, unit_price, tax_percent, qty) VALUES " +
                "(?,?,?,?,?)";
    }
    public static String getProduct() {
        return "SELECT unit_price,tax_percent FROM products where id=?";
    }
    public static String delete() {
        return "DELETE FROM commands WHERE id=? AND user_id=?";
    }
    public static String deleteCommand() {
        return "DELETE FROM commands WHERE id=?";
    }
    public static String deleteShoppingCard() {
        return "DELETE FROM shopping_card WHERE user_id=?";
    }
}
