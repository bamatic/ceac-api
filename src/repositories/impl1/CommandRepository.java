package repositories.impl1;

import contracts.ILogger;
import contracts.IRepository;
import entities.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandRepository implements IRepository<Command> {

    private User user;
    private final DataSource dataSource;
    private final ILogger logger;

    public CommandRepository(DataSource dataSource, ILogger logger) {
        this.dataSource = dataSource;
        this.user = new User(0,"");
        this.logger = logger;
    }
    @Override
    public List<Command> all(long limit) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            ResultSet rs;
            PreparedStatement st = CommandSt.allStatement(limit,  this.user, connection);
                rs = st.executeQuery();
                List<Command> commands = new ArrayList<>();
                long currentId = 0;
                Command command = new Command();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    if (id != currentId) {
                        currentId = id;
                        command = new Command(
                                id,
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
                        commands.add(command);
                    }
                    CommandLine commandLine = new CommandLine(
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
                    );
                    command.addLine(commandLine);

                }
                connection.close();
                return commands;

        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException indexing all commands: " +
                            e.getMessage()
            );
            return null;
        }

    }


    @Override
    public Command get(long id) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement st = CommandSt.getStatement(id, this.user, connection);
            ResultSet rs = st.executeQuery();
            Command command = null;
            boolean isFirst = true;
            while (rs.next()) {
                if (isFirst) {
                    command = new Command(
                            id,
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
                command.addLine(
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
            connection.close();
            return command;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException getting a command by id: " +
                            e.getMessage()
            );
            return null;
        }
    }

    @Override
    public Command create(Command command) {
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
                if (insertLinesSt != null) {
                    int[] codes = insertLinesSt.executeBatch();
                    for (int code : codes) {
                        System.out.println(code);
                    }
                    connection.commit();

                }
                return command;
            }

            connection.setAutoCommit(true);
            return null;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException creating a command: " +
                            e.getMessage()
            );
            if (connection != null) {
                try {
                    connection.rollback();
                    return null;
                } catch (SQLException ex) {
                    this.logger.message(
                            "SEVERE",
                            "SQLException rolling back database while creating a commands: " +
                                    ex.getMessage()
                    );
                    throw new RuntimeException(ex);
                }
            }
            return null;
        }
    }

    @Override
    public boolean update(long id, Command model) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(CommandSql.delete());
            st.setLong(1, id);
            st.setLong(2, this.user.getId());
            return st.executeUpdate() > 0;
        }
        catch (SQLException e) {
            this.logger.message(
                    "SEVERE",
                    "SQLException deleting command of id " + id + ": " +
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
