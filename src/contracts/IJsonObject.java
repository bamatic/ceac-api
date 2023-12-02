package contracts;

public interface IJsonObject {

    void put(String key, long longValue);
    void put(String key, int intValue);
    void put(String key, String strValue);
    void put(String key, Object object);
    void put(String key, boolean value);
    Object get(String key);
    String toJSONString();
    Object jsonObject();
    void setJsonObject(Object jsonObject);

}
