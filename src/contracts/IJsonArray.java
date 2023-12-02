package contracts;

import java.util.List;
/**
 * An abstraction for an array of JSON objects
 *
 */
public interface IJsonArray {
    /**
     * add a IJsonObject to the end of this IJsonArray
     *
     * @param jsonObject the IJsonObject to add to the array
     */
    void add(IJsonObject jsonObject);
    /**
     * serializes a json array
     *
     * @return a string representation of this IJSonArray
     */
    String toJSONString();
    /**
     * give the number of elements
     *
     * @return the number of elements
     */
    int size();
    /**
     * a List of IJsonObjects
     *
     * @return a List of JsonObjects or null
     */
    List<IJsonObject> jsonArray();
    /**
     * set an implementation of the IJsonArray
     *
     * @param jsonArray the implementation of the IJsonArray
     */
    void setJsonArray(Object jsonArray);
    /**
     * checks if an Object is a valid json array
     *
     * @return  true if the object is a json array
     */
    boolean isJsonArray(Object object);
}
