package services.impl1.JSON;

import contracts.IJsonObject;
import org.json.simple.JSONObject;

public class JsonObjectService implements IJsonObject {

    private JSONObject object;

    public JsonObjectService() {
        this.object = new JSONObject();
    }

    @Override
    public void put(String key, long longValue) {
        this.object.put(key, longValue);
    }

    @Override
    public void put(String key, int intValue) {
        this.object.put(key, intValue);
    }

    @Override
    public void put(String key, String strValue) {
        this.object.put(key, strValue);
    }

    @Override
    public void put(String key, Object object) {
        this.object.put(key, object);
    }

    @Override
    public void put(String key, boolean value) {
        this.object.put(key, value);
    }

    @Override
    public Object get(String key) {
        return this.object.get(key);
    }

    @Override
    public String toJSONString() {
        return this.object.toJSONString();
    }

    @Override
    public Object jsonObject() {
        return this.object;
    }
    @Override
    public void setJsonObject(Object jsonObject) {
        this.object = (JSONObject) jsonObject;
    }

    @Override
    public String toString() {
        return  this.object.toJSONString();
    }
}
