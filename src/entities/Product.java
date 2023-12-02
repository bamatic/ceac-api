package entities;

import contracts.IJsonObject;
import services.impl1.JSON.JsonObjectService;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Product {
    private long id = 0;
    private String name = null;
    private BigDecimal unitPrice = null;
    private int taxPercent = 0;
    private String imageUrl = null;
    private Date availableDate = null;
    private boolean isInShoppingCard = false;

    public Product(long id, String name, BigDecimal unitPrice, int taxPercent, String imageUrl, Date availableDate) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.taxPercent = taxPercent;
        this.imageUrl = imageUrl;
        this.availableDate = availableDate;
    }
    public Product(long id, String name, BigDecimal unitPrice, int taxPercent, String imageUrl, Date availableDate, boolean isInShoppingCard) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.taxPercent = taxPercent;
        this.imageUrl = imageUrl;
        this.availableDate = availableDate;
        this.isInShoppingCard = isInShoppingCard;
    }
    public Product(String name) {
        this.name = name;
    }
    public Product(long id) {
        this.id = id;
    }

    public Product(String name, BigDecimal unitPrice, int taxPercent, String imageUrl, Date availableDate) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.taxPercent = taxPercent;
        this.imageUrl = imageUrl;
        this.availableDate = availableDate;
    }
    public Product fromJSONObject() {
        return null;
    }
    public IJsonObject toJSONObject() {
       IJsonObject json = new JsonObjectService();
       json.put("id", this.id);
       json.put("name", this.name);
       json.put("unitPrice", this.unitPrice);
       json.put("taxPercent", this.taxPercent);
       json.put("imageUrl", this.imageUrl);
       DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
       json.put("availableDate", df.format(this.availableDate));
       json.put("isInShoppingCard", this.isInShoppingCard);
       return json;
    }
    public static Product fromJSONObject(IJsonObject payload) {
        return new Product((String) payload.get("name"));
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getTaxPercent() {
        return taxPercent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Date getAvailableDate() {
        return availableDate;
    }

    public boolean isInShoppingCard() {
        return isInShoppingCard;
    }

    public void setInShoppingCard(boolean inShoppingCard) {
        isInShoppingCard = inShoppingCard;
    }

    @Override
    public String toString() {

        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", taxPercent='" + taxPercent + '\'' +
                ", availableDate='" + availableDate + '\'' +
                '}';
    }
}
