package entities;

import contracts.IJsonObject;
import services.impl1.JSON.JsonObjectService;

import java.math.BigDecimal;


public class ShoppingCard {
    private long id;
    private User customer;
    private Product product;
    private int qty;

    public ShoppingCard(User customer, Product product, int qty) {
        this.customer = customer;
        this.product = product;
        this.qty = qty;
    }
    public ShoppingCard(long id, User customer, Product product, int qty) {
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.qty = qty;
    }

    public IJsonObject toJSONObject() {
        IJsonObject json = new JsonObjectService();
        json.put("id", this.id);
        json.put("customer", this.customer.toJSONObject());
        json.put("product", this.product.toJSONObject());
        json.put("qty", this.qty);
        json.put("preTax", this.preTax());
        json.put("tax", this.tax());
        json.put("price", this.price());
        return json;
    }
    public static ShoppingCard fromJSONObject(User user, IJsonObject payload) {
        if (!ShoppingCard.validate(payload))
            return null;
        return new ShoppingCard(
                user,
                new Product((long) payload.get("product_id")),
                (int) (long) payload.get("qty")
        );
    }
    private static boolean validate(IJsonObject json) {
        Long product_id = (Long) json.get("product_id");
        if (product_id == null || product_id < 0)
            return false;
        Long qty = (Long) json.get("qty");
        return (qty != null && qty > 0);
    }

    public BigDecimal preTax() {
        return this.product.getUnitPrice().multiply(new BigDecimal(this.qty));
    }
    public BigDecimal tax() {
        return this.preTax()
                .multiply(BigDecimal.valueOf(this.product.getTaxPercent()))
                .divide(BigDecimal.valueOf(100));
    }
    public BigDecimal price() {
        return this.preTax().add(this.tax());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {

        return "ShoppingCard{" +
                "product=" + product.toString() +
                ", qty=" + qty +
                ", pretax=" + this.preTax() +
                '}';
    }
}
