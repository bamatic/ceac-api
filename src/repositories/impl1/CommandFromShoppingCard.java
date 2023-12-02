package repositories.impl1;

import contracts.ICommandFromShoppingCard;
import contracts.ILogger;
import contracts.IRepository;
import entities.*;

import javax.sql.DataSource;
import java.sql.*;

public class CommandFromShoppingCard implements ICommandFromShoppingCard {

    private User user;
    private final DataSource dataSource;
    private final ILogger logger;

    public CommandFromShoppingCard(DataSource dataSource, ILogger logger) {
        this.dataSource = dataSource;
        this.user = new User(0,"");
        this.logger = logger;
    }
    @Override

    public Command createSP(Command command) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement createCommandSt = CommandSt.createCommandStatement(this.user, command, connection);
            createCommandSt.executeUpdate();
            ResultSet ids = createCommandSt.getGeneratedKeys();
            if (ids.next()) {
                command.setId(ids.getLong(1));
                PreparedStatement insertLinesSt = CommandSt.createCommandLineStatement(command, connection);
                if (insertLinesSt == null)  {

                    try {
                        this.logger.message(
                                "SEVERE",
                                "Impossible to insert line while creating a command from the shopping card "
                        );
                        connection.rollback();
                        connection.close();
                        return null;
                    } catch (SQLException ex) {
                        this.logger.message(
                                "SEVERE",
                                "SQLException rolling back while creating a command from the sc: " +
                                        ex.getMessage()
                        );
                        throw new RuntimeException(ex);
                    }
                }
                insertLinesSt.executeBatch();
                PreparedStatement deleteShoppingCardSt = CommandSt.deleteShoppingCard(this.user, connection);
                if (deleteShoppingCardSt.executeUpdate() > 0) {
                    connection.commit();
                    if (!this.verifyPrice(connection, command)) {
                        this.cancelCommand(connection, command);
                        connection.close();
                        return null;
                    }
                    connection.close();
                    return command;
                }
                else  {
                    try {
                        this.logger.message(
                                "SEVERE",
                                "Impossible to delete de shopping card while creating a command from the shopping card "
                        );
                        connection.rollback();
                        connection.close();
                        return null;
                    } catch (SQLException ex) {
                        this.logger.message(
                                "SEVERE",
                                "SQLException rolling back while creating a command from the sc: " +
                                        ex.getMessage()
                        );
                        throw new RuntimeException(ex);
                    }
                }
            }

            connection.setAutoCommit(true);
            connection.close();
            return null;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException creating a command from the sc: " +
                            e.getMessage()
            );
            if (connection != null) {
                try {
                    connection.rollback();
                    return null;
                } catch (SQLException ex) {
                    this.logger.message(
                            "SEVERE",
                            "SQLException rolling back while creating an address for the connected user: " +
                                    ex.getMessage()
                    );
                    throw new RuntimeException(ex);
                }
            }
            return null;
        }
    }

    private void cancelCommand(Connection connection, Command command) throws SQLException {
        PreparedStatement deleteSt = CommandSt.deleteCommand(command.getId(), connection);
        if (deleteSt.executeUpdate() == 1) {
            PreparedStatement addToShoppingCardSt = connection.prepareStatement(ShoppingCardSql.create());
            for (CommandLine commandLine: command.getLines()) {
                addToShoppingCardSt.setLong(1, commandLine.getProduct().getId());
                addToShoppingCardSt.setLong(2, this.user.getId());
                addToShoppingCardSt.setInt(3, commandLine.getQty());
                addToShoppingCardSt.addBatch();
            }
            int[] updates = addToShoppingCardSt.executeBatch();
            if ( updates.length > 0) {
                connection.commit();
                this.logger.message(
                        "INFO",
                        "command id " + command.getId() + " erased and shopping card restored " +
                                "after a buyerPrice issue"
                );
            }
            else {
                try {
                    connection.rollback();
                    this.logger.message(
                            "INFO",
                            "SQLException rolling back while re creating shopping card" +
                                    " a buyerPrice issue after deleting command id " + command.getId()
                    );
                } catch (SQLException ex) {
                    this.logger.message(
                            "SEVERE",
                            "SQLException rolling back while creating an address for the connected user: " +
                                    ex.getMessage()
                    );
                    throw new RuntimeException(ex);
                }
            }
        }
        else {
            this.logger.message(
                    "SEVERE",
                    "unable to delete created command with buyerPrice issue commandId " + command.getId()
            );
        }

    }

    private boolean verifyPrice(Connection connection, Command command) throws SQLException {
        PreparedStatement getSt = CommandSt.getStatement(command.getId(), this.user, connection);
        ResultSet rs = getSt.executeQuery();
        Command insertedCommand = null;
        boolean isFirst = true;
        while (rs.next()) {
            if (isFirst) {
                insertedCommand = new Command(
                        command.getId(),
                        rs.getString("command_name"),
                        rs.getDate("command_date"),
                        this.user,
                        new Address(
                                (String) rs.getString("delivery_town"),
                                (String) rs.getString("delivery_region"),
                                (String) rs.getString("delivery_state"),
                                (String) rs.getString("delivery_postal_code"),
                                (String) rs.getString("delivery_address")
                        ),
                        new Address(
                                (String) rs.getString("invoice_town"),
                                (String) rs.getString("invoice_region"),
                                (String) rs.getString("invoice_state"),
                                (String) rs.getString("invoice_postal_code"),
                                (String) rs.getString("invoice_address")
                        ),
                        rs.getDate("delivery_date"),
                        Command.State.valueOf(rs.getString("state"))
                );
                isFirst = false;
            }
            insertedCommand.addLine(
                    new CommandLine(
                            rs.getLong("command_line_id"),
                            new Product(
                                    rs.getLong("id"),
                                    rs.getString("name"),
                                    rs.getBigDecimal("unit_price"),
                                    rs.getInt("tax_percent"),
                                    rs.getString("image"),
                                    rs.getDate("available_date")
                            ),
                            rs.getInt("qty")
                    )
            );
        }
        return insertedCommand != null && insertedCommand.price().equals(command.getBuyerPrice());
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
