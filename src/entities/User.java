package entities;

import contracts.IJsonObject;
import services.impl1.JSON.JsonObjectService;
import services.impl1.jwt.PasswordHashService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private long id;
    private String taxId;
    private String name;
    private String surname;
    private Address deliveryAddress;
    private Address invoiceAddress;
    private byte[] password;
    private String strPassword;

    private String email;
    private String role = "customer";

    public User(String taxId, String name, String surname, Address deliveryAddress, Address invoiceAddress, String email) {
        this.taxId = taxId;
        this.name = name;
        this.surname = surname;
        this.deliveryAddress = deliveryAddress;
        this.invoiceAddress = invoiceAddress;
        this.email = email;
    }

    public User(long id, String taxId, String name, String surname, Address deliveryAddress, Address invoiceAddress, String email, String role) {
        this.id = id;
        this.taxId = taxId;
        this.name = name;
        this.surname = surname;
        this.deliveryAddress = deliveryAddress;
        this.invoiceAddress = invoiceAddress;
        this.email = email;
        this.role = role;
    }
    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public User(long id) {
        this.id = id;
    }
    public User(long id, String email, byte[] password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User(String taxId, String name, String surname, Address deliveryAddress, Address invoiceAddress, String strPassword, String email, String role) {
        this.id = 0;
        this.taxId = taxId;
        this.name = name;
        this.surname = surname;
        this.deliveryAddress = deliveryAddress;
        this.invoiceAddress = invoiceAddress;
        this.strPassword = strPassword;
        this.email = email;
        this.role = role;
    }
    public User(String taxId, String name, String surname, Address deliveryAddress, Address invoiceAddress, String strPassword, String email) {
        this.id = 0;
        this.taxId = taxId;
        this.name = name;
        this.surname = surname;
        this.deliveryAddress = deliveryAddress;
        this.invoiceAddress = invoiceAddress;
        this.strPassword = strPassword;
        this.email = email;
    }

    public static User fromJSONObject(IJsonObject json) {
        if (!User.validate(json))
            return null;
        return new User(
                (String) json.get("tax_id"),
                (String) json.get("name"),
                (String) json.get("surname"),
                new Address(
                        (long) json.get("delivery_address_id")
                ),
                new Address(
                        (long) json.get("invoice_address_id")
                ),
                (String) json.get("password"),
                (String) json.get("email")
        );
    }
    private static boolean validate(IJsonObject json) {
        String taxId = (String) json.get("tax_id");
        if (taxId == null || taxId.length() > 15)
            return false;
        String name = (String) json.get("name");
        if (name == null || name.length() > 80)
            return false;
        String surname = (String) json.get("surname");
        if (surname == null || surname.length() > 80)
            return false;
        Long deliveryAddressId = (Long) json.get("delivery_address_id");
        if (deliveryAddressId == null)
            return false;
        Long invoiceAddressId = (Long) json.get("invoice_address_id");
        if (invoiceAddressId == null)
            return false;
        String password = (String) json.get("password");
        if (password != null && !User.validatePassword(password) )
            return false;
        String email = (String) json.get("email");
        return (email !=null && email.length() <120);
    }
    public static boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\^\\$\\!@#%&-_=+<>/\\?{\\}\\[\\]\\(\\)]).{8,}$");
        Matcher matcher = pattern.matcher(password);
        return password.length() > 7 && password.length() < 32 && matcher.matches();
    }
    public IJsonObject toJSONObject() {
        IJsonObject json = new JsonObjectService();
        json.put("id", this.id);
        json.put("taxId", this.taxId);
        json.put("name", this.name);
        json.put("surname", this.surname);
        json.put("deliveryAddress", this.deliveryAddress.toJSONObject());
        json.put("invoiceAddress", this.invoiceAddress.toJSONObject());
        json.put("email", this.email);
        json.put("role", this.role);
        return json;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    public String getSurname() {
        return surname;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public boolean isAdmin() {
        return this.role.equals("admin");
    }
    public boolean verify(String email, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] newHash = PasswordHashService.hashPassword(password);
        return Arrays.equals(newHash, this.password) && email.equals(this.email);
    }

}
