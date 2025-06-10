package project;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class QueryParser {

    /**
     * Parsea los parámetros de una URL o query string.
     *
     * @param input puede ser:
     *              - Un String como "key=value&key2=value2"
     *              - Una URL completa como "http://example.com?query=...&desde=..."
     * @return Map<String, String> con los parámetros parseados
     */
    public static Map<String, String> parseQueryParams(String input) {
        if (input == null || input.isEmpty()) {
            return Collections.emptyMap();
        }

        // Extraer solo el query string si es una URL completa
        String queryString = input.contains("?") ? input.split("\\?", 2)[1] : input;

        // Reemplazar entidades HTML comunes (por si viene &amp;)
        queryString = queryString.replace("&amp;", "&");

        Map<String, String> params = new HashMap<>();

        for (String param : queryString.split("&")) {
            if (param.isEmpty()) continue;

            String[] pair = param.split("=", 2); // máximo 2 partes
            String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
            String value = (pair.length > 1)
                    ? URLDecoder.decode(pair[1], StandardCharsets.UTF_8)
                    : null;

            params.put(key, value);
        }

        return params;
    }
}
