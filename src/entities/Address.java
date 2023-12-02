package entities;

import contracts.IJsonObject;
import services.impl1.JSON.JsonObjectService;

public class Address {
    private long id;
    private String town;
    private String region;
    private String state;
    private String postalCode;
    private String address;

    public Address(String town, String region, String state, String postalCode, String address) {
        this.id = 0;
        this.town = town;
        this.region = region;
        this.state = state;
        this.postalCode = postalCode;
        this.address = address;
    }
    public Address(long id, String town, String region, String state, String postalCode, String address) {
        this.id = id;
        this.town = town;
        this.region = region;
        this.state = state;
        this.postalCode = postalCode;
        this.address = address;
    }
    public Address(long id) {
        this.id = id;
    }

    public static Address fromJSONObject(IJsonObject json) {
        if (Address.validate(json))
            return new Address(
                    (long) json.get("id"),
                    (String) json.get("town"),
                    (String) json.get("region"),
                    (String) json.get("state"),
                    (String) json.get("postal_code"),
                    (String) json.get("address")
            );
        return null;
    }
    public IJsonObject toJSONObject() {
        IJsonObject json = new JsonObjectService();
        json.put("id", this.id);
        json.put("town", this.town);
        json.put("region", this.region);
        json.put("state", this.state);
        json.put("postalCode", this.postalCode);
        json.put("address", this.address);
        return json;
    }

    @Override
    public String toString() {
        return "Address{" +
                ", town='" + town + '\'' +
                ", region='" + region + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private static boolean validate(IJsonObject json) {
        Long id =(Long) json.get("id");
        if (id == null || id < 0)
            return false;
        String town = (String) json.get("town");
        if (town == null|| town.length() > 80)
            return false;
        String region = (String) json.get("region");
        if (region == null|| region.length() > 25)
            return false;
        String state = (String) json.get("state");
        if (state == null|| state.length() > 25)
            return false;
        String postalCode = (String) json.get("postal_code");
        if (postalCode == null|| postalCode.length() != 5)
            return false;
        String address = (String) json.get("address");
        return address != null && address.length() <= 255;
    }
}
