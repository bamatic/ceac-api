package contracts;

/**
 * parses and converts to an IJsonObject the
 * given json String
 */
public interface IJsonParser {
    /**
     * get a String representation of the JSONObject
     *
     * @param jsonString the json String to parse
     * @return  a well-formed IJsonObject or null
     *
     */
    IJsonObject parse(String jsonString);
}
