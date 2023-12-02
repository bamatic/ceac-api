package entities;

import contracts.IJsonArray;
import contracts.IJsonObject;
import services.impl1.JSON.JsonArrayService;
import services.impl1.JSON.JsonObjectService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Command {
    public enum State {
        CREATED,
        TRANS,
        FINISH
    }
    private long id = 0;
    private String name;
    private List<CommandLine> lines =  new ArrayList<>();
    private Date commandDate;
    private User customer;
    private Address deliveryAddress;
    private Address invoiceAddress;

    private Date deliveryDate;
    private State state = State.CREATED;
    private BigDecimal buyerPrice = BigDecimal.valueOf(0);

    public Command(long id, String name, Date commandDate, User customer, Address deliveryAddress, Address invoiceAddress, Date deliveryDate, State state) {
        this.id = id;
        this.name = name;
        this.commandDate = commandDate;
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
        this.invoiceAddress = invoiceAddress;
        this.deliveryDate = deliveryDate;
        this.state = state;
    }

    public Command(String name, Date commandDate, User customer, Address deliveryAddress, Address invoiceAddress, Date deliveryDate, State state) {
        this.name = name;
        this.commandDate = commandDate;
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
        this.invoiceAddress = invoiceAddress;
        this.deliveryDate = deliveryDate;
        this.state = state;
    }
    public Command() {

    }

    @Override
    public String toString() {
        return "Command{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lines=" + lines.size() +
                ", commandDate=" + commandDate +
                ", customer=" + customer +
                ", deliveryAddress=" + deliveryAddress +
                ", invoiceAddress=" + invoiceAddress +
                ", deliveryDate=" + deliveryDate +
                ", state=" + state +
                '}';
    }

    public void addLine(CommandLine line) {
        this.lines.add(line);
    }
    public IJsonObject toJSONObject() {
        IJsonObject command = new JsonObjectService();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        command.put("id", this.id);
        command.put("name", this.name);
        command.put("customer", this.customer.toJSONObject());
        command.put("command_date", df.format(this.commandDate));
        command.put("delivery_date", df.format(this.deliveryDate));
        command.put("state", this.state.toString());
        command.put("deliveryAddress", this.deliveryAddress.toJSONObject());
        command.put("invoiceAddress", this.invoiceAddress.toJSONObject());

        IJsonArray lines = new JsonArrayService();
        for (CommandLine line : this.lines) {
            lines.add(line.toJSONObject());
        }
        command.put("lines", lines);
        command.put("preTax",this.preTax());
        command.put("tax",this.tax());
        command.put("price",this.price());

        return command;
    }
    public static Command fromJSONObject(User user, IJsonObject payload) {
        if (!Command.validate(payload))
            return null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date commandDate, deliveryDate;
        try {
            commandDate = df.parse((String) payload.get("commandDate"));
            deliveryDate = df.parse((String) payload.get("deliveryDate"));
            Command command = new Command(
                    (String) payload.get("name"),
                    commandDate,
                    user,
                    user.getDeliveryAddress(),
                    user.getInvoiceAddress(),
                    deliveryDate,
                    State.CREATED
            );
            Object objBuyerPrice = payload.get("buyerPrice");
            if (objBuyerPrice instanceof Double) {
                command.setBuyerPrice(BigDecimal.valueOf((Double) objBuyerPrice));
            }
            else {
                BigDecimal buyerPrice = BigDecimal.valueOf(((Long) objBuyerPrice).doubleValue());
                buyerPrice = buyerPrice.setScale(2, RoundingMode.HALF_EVEN);
                command.setBuyerPrice(buyerPrice);
            }
            IJsonArray jsonLines = new JsonArrayService();
            jsonLines.setJsonArray(payload.get("lines"));
            List<IJsonObject> j = jsonLines.jsonArray();
            for (IJsonObject obj : jsonLines.jsonArray()) {
                command.lines.add(CommandLine.fromJSONObject(obj));
            }
            return command;
        }
        catch (ParseException e) {
            return null;
        }
    }
    private static boolean validate(IJsonObject json) {

        String commandDate = (String) json.get("commandDate");
        if (commandDate == null ) return false;
        String deliveryDate = (String) json.get("deliveryDate");
        if (deliveryDate == null ) return false;
        String name = (String) json.get("name");
        if (name == null || name.length() > 80) return false;

        try {
            Object obj = json.get("buyerPrice");
            if (obj == null) return false;
            if ( !(obj instanceof Long) && !(obj instanceof Double))
                return false;
        }
        catch (NumberFormatException e) {
            return false;
        }
        Object lines = json.get("lines");
        IJsonArray arrLines = new JsonArrayService();
        if (!arrLines.isJsonArray(lines)) return false;
        arrLines.setJsonArray(lines);
        for (IJsonObject jsonObject: arrLines.jsonArray()) {
            if (CommandLine.fromJSONObject(jsonObject) == null) {
                return false;
            }
        }
        return true;
     }
    public String xml() {
        return "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<CommandLine> getLines() {
        return lines;
    }

    public void setLines(List<CommandLine> lines) {
        this.lines = lines;
    }

    public Date getCommandDate() {
        return commandDate;
    }

    public void setCommandDate(Date commandDate) {
        this.commandDate = commandDate;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Address getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(Address invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBuyerPrice() {
        return buyerPrice;
    }

    public void setBuyerPrice(BigDecimal buyerPrice) {
        this.buyerPrice = buyerPrice;
    }

    public BigDecimal preTax() {
        BigDecimal preTax = new BigDecimal(0);
        for (CommandLine line : this.lines) {
            preTax = preTax.add(line.preTax());
        }
        return preTax;
    }
    public BigDecimal tax() {
        BigDecimal tax = new BigDecimal(0);
        for (CommandLine line: this.lines) {
            tax = tax.add(line.tax());
        }
        return tax;
    }
    public BigDecimal price() {
        return (this.preTax()).add(this.tax());
    }

    public String toXML() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
                "<?xml-stylesheet type='text/xsl' href='xsl/invoice.xsl'?>" +
                "<invoice>\n" +
                "  <invoicenumber>" + this.id + "</invoicenumber>\n" +
                "  <invoicedate>" + this.commandDate + "</invoicedate>\n" +
                "  <commandname> " + this.name + "</commandname>\n" +
                "  <deliverydate>" + this.deliveryDate + "</deliverydate>\n" +
                "  <address type=\"shipto\">\n" +
                "    <name> " + this.customer.getName() + "</name>\n" +
                "    <street> " + this.deliveryAddress.getAddress() + "</street>\n" +
                "    <zipcode>" + this.deliveryAddress.getPostalCode() +"</zipcode>\n" +
                "    <city>" + this.deliveryAddress.getTown() + "</city>\n" +
                "  </address>\n" +
                "  <address type=\"billto\">\n" +
                "    <name> " + this.customer.getName() + "</name>\n" +
                "    <street> " + this.deliveryAddress.getAddress() + "</street>\n" +
                "    <zipcode>" + this.deliveryAddress.getPostalCode() +"</zipcode>\n" +
                "    <city>" + this.deliveryAddress.getTown() + "</city>\n" +
                "  </address>\n";
        if (!this.lines.isEmpty()) {
            xml += "  <lineitems>\n";
            for (CommandLine line : this.lines) {
                xml +=  "    <lineitem>\n" +
                        "      <quantity>" + line.getQty() + "</quantity>\n" +
                        "      <description>" + line.getProduct().getName() + "</description>\n" +
                        "      <unitprice>" + line.getUnitPrice() + "</unitprice>\n" +
                        "      <taxpercent>" + line.getTaxPercent() + "%</taxpercent>\n" +
                        "    </lineitem>\n" ;
            }
        }
        xml +=  "  </lineitems>\n" +
                "  <subtotal>" + this.preTax() + "</subtotal>\n" +
                "  <tax>"+ this.tax() + "</tax>\n" +
                "  <total>" + this.price() + "</total>\n" +
                "</invoice>";
        return xml;
    }
}
