package http;

import com.sun.net.httpserver.HttpExchange;
import contracts.IJsonObject;
import contracts.IJsonParser;
import contracts.ILogger;
import services.impl1.JSON.JsonParserService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * extraction of method, payload, query params
 * and identifier from the http request
 */
public class HttpRequestManager {
    private final String route;
    private final long id;
    private final IJsonObject payload;
    private final String method;
    private final long limit;
    private final boolean deleteSP;

    private final String customerAddress;
    private final ILogger logger;

    /**
     * Extracts info from the httpExchange
     *
     * @param httpExchange the Http exchange
     * @param logger for logging
     *
     */
    public HttpRequestManager(HttpExchange httpExchange, ILogger logger) {
        this.logger = logger;
        this.route = this.extractRoute(httpExchange.getRequestURI());

        this.id = this.extractId(httpExchange.getRequestURI());

        this.method = httpExchange.getRequestMethod();
        this.payload = this.extractPayload(httpExchange);
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null) {
            this.limit = 0;
            this.deleteSP = false;
            this.customerAddress = null;
        }
        else {
            Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());
            this.limit = this.limit(params);
            this.deleteSP = this.deleteSP(params);
            this.customerAddress = this.customerAddress(params);
        }
    }

    public String getRoute() {
        return route;
    }

    public long getId() {
        return id;
    }

    public IJsonObject getPayload() {
        return payload;
    }

    public String getMethod() {
        return method;
    }

    public long getLimit() {
        return limit;
    }

    public boolean isDeleteSP() {
        return deleteSP;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }
    public boolean isDelivery() {
        return customerAddress.equals("delivery");
    }
    public boolean isInvoice() {
        return customerAddress.equals("invoice");
    }
    private IJsonObject extractPayload(HttpExchange exchange) {
        // Get the request body
        InputStream requestBody = exchange.getRequestBody();

        try {
            byte[] bytes = requestBody.readAllBytes();
            String input = new String(bytes);
            if (!input.isEmpty()) {
                IJsonParser parser = new JsonParserService(this.logger);
                return parser.parse(input);
            }
            return null;

        } catch (IOException e) {
            logger.message(
                    "SEVERE",
                    "IO Error while extracting json payload from HTTP request: " + e.getMessage()
            );
            
            return null;
        }

    }
    private int extractId(URI requestURI) {
        String path = requestURI.getPath();
        String strId = path.substring(path.lastIndexOf('/') + 1);
        if (strId.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(strId);
        }
        catch (NumberFormatException e) {
            logger.message(
                    "INFO",
                    "NumberFormatException While parsing url id : " + e.getMessage()
            );
            
            return -1;
        }
    }

    private String extractRoute(URI requestURI) {
        return "/test";
    }
    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }
    private long limit(Map<String, String> params) {
        String limit = params.get("limit");
        if (limit == null) {
            return 0;
        }
        try {
            return Long.parseLong(params.get("limit"));
        }
        catch (NumberFormatException e) {
            logger.message(
                    "INFO",
                    "NumberFormatException While parsing query limit param : " + e.getMessage()
            );
            
            return 0;
        }
    }
    private boolean deleteSP(Map<String, String> params) {
        String deleteSP = params.get("deleteSP");
        if (deleteSP == null) {
            return false;
        }
        return deleteSP.equals("true");
    }
    private String customerAddress(Map<String, String> params) {
        return params.get("customerAddress");
    }


}
