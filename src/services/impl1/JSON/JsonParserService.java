package services.impl1.JSON;

import contracts.IJsonObject;
import contracts.IJsonParser;
import contracts.ILogger;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParserService implements IJsonParser {
    JSONParser parser = new JSONParser();
    ILogger logger;
    public JsonParserService(ILogger logger) {
        this.logger = logger;
    }
    @Override
    public IJsonObject parse(String jsonString) {
        try {
            IJsonObject jsonObject = new JsonObjectService();
            jsonObject.setJsonObject(parser.parse(jsonString));
            return jsonObject;
        }
        catch (ParseException e) {
            logger.message("SEVERE", "ERROR WHILE PARSING JSON STRING "+ jsonString + ": " + e.getMessage());
            
           return null;
        }
    }
}
