package services.impl1.jwt;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenExtractorService {
    public static Map<String, String> extractCookies(HttpExchange exchange)  {

        Headers requestHeaders = exchange.getRequestHeaders();
        Map<String, String> authorizations = new HashMap<>();
        List<String> authorizationHeader = requestHeaders.get("Authorization");
        if (authorizationHeader != null) {
            for (String authorizationsString : authorizationHeader) {
                String[] parts = authorizationsString.split("=");
                if (parts.length > 1) {
                    authorizations.put(parts[0], parts[1]);
                }
            }
            return authorizations;
        }
        else {
            return null;
        }

    }

    public static String getToken(HttpExchange exchange, String tokenName) {
         Map<String, String> map = TokenExtractorService.extractCookies(exchange);
         if (map != null)
             return map.get(tokenName);
         return null;
    }
}
