package services.impl1.JSON;

import contracts.IJsonArray;
import contracts.IJsonObject;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class JsonArrayService implements IJsonArray {
    private JSONArray jsonArray = new JSONArray();
    @Override
    public void add(IJsonObject jsonObject) {
        this.jsonArray.add(jsonObject.jsonObject());
    }

    @Override
    public String toJSONString() {
        return this.jsonArray.toJSONString();
    }
    @Override
    public int size() {
        return this.jsonArray.size();
    }
    @Override
    public List<IJsonObject> jsonArray() {
        List<IJsonObject> list = new ArrayList<>();
        for (Object jsonObject: this.jsonArray) {
            IJsonObject jsonObject1 = new JsonObjectService();
            jsonObject1.setJsonObject(jsonObject);
            list.add(jsonObject1);
        }
        return list;
    }

    @Override
    public void setJsonArray(Object jsonArray) {
        this.jsonArray = (JSONArray) jsonArray;
    }

    @Override
    public boolean isJsonArray(Object object) {
        return object instanceof JSONArray;
    }

    @Override
    public String toString() {
        return this.jsonArray.toJSONString();
    }
}
