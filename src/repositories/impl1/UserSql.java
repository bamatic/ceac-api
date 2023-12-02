package repositories.impl1;
public class UserSql {

    public static String all(long limit) {
        String strSql =   "SELECT" +
                "u.name,u.surname,u.tax_id,u.email,u.role," +
                "d.id as delivery_id, d.town as delivery_town, d.region as delivery_region, d.state as delivery_state, d.address as delivery_address," +
                "d.postal_code as delivery_postal_code," +
                "i.id as invoice_id, i.town as invoice_town, i.region as invoice_region, i.state as invoice_state, i.address as invoice_address," +
                "i.postal_code as invoice_postal_code " +
                "FROM users u " +
                "INNER JOIN addresses d ON d.id = u.delivery_address_id " +
                "INNER JOIN addresses i ON i.id = u.invoice_address_id ";
        if (limit > 0) {
            strSql += " LIMIT ?";
        }
        return strSql;
    }
    public static String get() {
        return   "SELECT " +
                "u.id, u.name,u.surname,u.tax_id,u.email,u.role," +
                "d.id as delivery_id, d.town as delivery_town, d.region as delivery_region, d.state as delivery_state, d.address as delivery_address," +
                "d.postal_code as delivery_postal_code," +
                "i.id as invoice_id, i.town as invoice_town, i.region as invoice_region, i.state as invoice_state, i.address as invoice_address," +
                "i.postal_code as invoice_postal_code " +
                "FROM users u " +
                "INNER JOIN addresses d ON d.id = u.delivery_address_id " +
                "INNER JOIN addresses i ON i.id = u.invoice_address_id " +
                "WHERE u.id=? ";
    }
    public static String create() {
        return "INSERT INTO users (name, surname, tax_id, delivery_address_id, invoice_address_id, email, password) " +
                "VALUES (?,?,?,?,?,?)";
    }
    public static String updateAll() {
        return "UPDATE users set delivery_address_id=?, invoice_address_id=?, email=?, password=?" +
                "WHERE id=?";
    }
    public static String updateEmailPassword() {
        return "UPDATE users set email=?, password=?" +
                "WHERE id=?";
    }
    public static String updateEmail() {
        return "UPDATE users set email=?" +
                "WHERE id=?";
    }
    public static String getUserAndPassword() {
        return   "SELECT " +
                "id, email, password " +
                "FROM users u " +
                "WHERE email=? and password=?";
    }
}
