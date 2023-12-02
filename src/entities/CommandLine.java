package entities;

import contracts.IJsonObject;
import services.impl1.JSON.JsonObjectService;

import java.math.BigDecimal;

public class CommandLine {
    private final long id;

    private Product product;
    private BigDecimal unitPrice;
    private int taxPercent = 0;
    private int qty = 0;

    public CommandLine(long id, Product product, int qty) {
        this.id = id;
        this.product = product;
        this.taxPercent = product.getTaxPercent();
        this.unitPrice = product.getUnitPrice();
        this.qty = qty;
    }
    public CommandLine(Product product, int qty) {
        this.id = 0;
        this.product = product;
        this.taxPercent = product.getTaxPercent();
        this.unitPrice = product.getUnitPrice();
        this.qty = qty;
    }

    public BigDecimal preTax() {
        return this.unitPrice.multiply(new BigDecimal(this.qty));
    }
    public BigDecimal tax() {
        return this.preTax()
                .multiply(BigDecimal.valueOf(this.product.getTaxPercent()))
                .divide(BigDecimal.valueOf(100));
    }
    public BigDecimal price() {
        return this.preTax().add(this.tax());
    }
    public IJsonObject toJSONObject() {
        IJsonObject line = new JsonObjectService();
        line.put("id", this.id);
        line.put("product", this.product.toJSONObject());
        line.put("unitPrice", this.unitPrice);
        line.put("taxPercent", this.taxPercent);
        line.put("qty", this.qty);
        return line;
    }
    public static CommandLine fromJSONObject(IJsonObject payload) {
        if (!CommandLine.validate(payload))
            return null;
        long product_id = (long) payload.get("product_id");
        int qty = (int) (long) payload.get("qty");
        return new CommandLine(new Product(product_id), qty);
    }
    private static boolean validate(IJsonObject json) {
        Long product_id = (Long) json.get("product_id");
        if (product_id == null || product_id < 0)
            return false;
        Long qty = (Long) json.get("qty");
        return (qty != null && qty > 0);
     }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getTaxPercent() {
        return taxPercent;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
