package contracts;

/**
 * An abstraction to manage Json Objects
 *
 */
public interface IJsonObject {

    /**
     * set a String key long value pair
     *
     * @param key the string key
     * @param longValue the long value
     *
     */
    void put(String key, long longValue);
    /**
     * set a String key integer value pair
     *
     * @param key the string key
     * @param intValue the int value
     *
     */
    void put(String key, int intValue);
    /**
     * set a String key String value pair
     *
     * @param key the string key
     * @param strValue the string value
     *
     */
    void put(String key, String strValue);
    /**
     * set a String key Object value pair
     *
     * @param key the string key
     * @param object the Object value
     *
     */
    void put(String key, Object object);
    /**
     * set a String key boolean value pair
     *
     * @param key the string key
     * @param value the boolean value
     *
     */
    void put(String key, boolean value);
    /**
     * get the Object value associated with a String key
     *
     * @param key the string key
     * @return object the Object value
     *
     */
    Object get(String key);
    /**
     * get a String representation of the JSONObject
     *
     *
     * @return  longValue the long value
     *
     */
    String toJSONString();
    /**
     * get the Object representation of the JSONObject
     *
     *
     * @return  longValue the long value
     *
     */
    Object jsonObject();
    /**
     * set the JSONObject
     *
     *
     * @param   jsonObject the Object value
     *
     */
    void setJsonObject(Object jsonObject);

}
