package repositories.mock;

import contracts.IRepository;
import entities.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandRepository implements IRepository<Command> {

    private User user;
    private final DataSource dataSource;
    public CommandRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.user = new User(0,"");
    }
    @Override
    public List<Command> all(long limit) {
        Address ca = new Address(1, "Lekeitio","Bizkaia","Euskadi", "48280", "Dendari");
        Address da = new Address(2, "Lekeitio","Bizkaia","Euskadi", "48280", "Dendari");
        User customer = new User(1,"1606163J","Oumar", "fuentes",da,ca,"vzugadi","admin");
        Product p1 = new Product(1,"colza", BigDecimal.valueOf(5),21,"",new Date());
        Product p2 = new Product(1,"oliva", BigDecimal.valueOf(50),21,"",new Date());

        Command c1 = new Command(5,"aceites ricos", new Date(), customer, da,ca,new Date(), Command.State.CREATED);
        c1.addLine(new CommandLine(p1,5));
        c1.addLine(new CommandLine(p2,24));
        List<Command> commands = new ArrayList<>();
        commands.add(c1);
        return commands;
    }

    @Override
    public Command get(long id) {
        Address ca = new Address(1, "Lekeitio","Bizkaia","Euskadi", "48280", "Dendari");
        Address da = new Address(2, "Lekeitio","Bizkaia","Euskadi", "48280", "Dendari");
        User customer = new User(1,"1606163J","Oumar", "fuentes",da,ca,"","vzugadi");
        Product p1 = new Product(1,"colza", BigDecimal.valueOf(5),21,"",new Date());
        Product p2 = new Product(1,"oliva", BigDecimal.valueOf(50),21,"",new Date());

        Command c1 = new Command(id,"aceites ricos", new Date(), customer, da,ca,new Date(), Command.State.CREATED);
        c1.addLine(new CommandLine(p1,5));
        c1.addLine(new CommandLine(p2,24));
        return c1;
    }

    @Override
    public Command create(Command model) {
        return model;
    }

    @Override
    public boolean update(long id, Command model) {
        return true;
    }

    @Override
    public boolean delete(long id) {
        return true;
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
