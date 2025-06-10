package project;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import java.util.*;
public class ResponseUtils {


    public static void respuestaJSON(HttpExchange exchange, int code, Object data) throws IOException {
        String jsonResponse;

        if (data == null) {
            jsonResponse = "{\"error\": \"Datos nulos\"}";
        } else if (data instanceof String) {
            jsonResponse = (String) data;
        } else if (data instanceof Map) {
            jsonResponse = mapToJson((Map<?, ?>) data);
        } else if (data instanceof List) {
            jsonResponse = listToJson((List<?>) data);
        } else {
            jsonResponse = "{\"error\": \"Tipo de dato no soportado: " + data.getClass().getSimpleName() + "\"}";
        }

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String listToJson(List<?> list) {
        StringBuilder json = new StringBuilder("[");
        for (Object item : list) {
            if (item instanceof Map) {
                json.append(mapToJson((Map<?, ?>) item));
            } else {
                json.append("\"").append(item).append("\"");
            }
            json.append(",");
        }

        if (!list.isEmpty()) {
            json.deleteCharAt(json.length() - 1); // eliminar última coma
        }

        return json.append("]").toString();
    }

    private static String mapToJson(Map<?, ?> map) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            json.append("\"")
                    .append(entry.getKey())
                    .append("\":\"")
                    .append(entry.getValue())
                    .append("\",");
        }

        if (!map.isEmpty()) {
            json.deleteCharAt(json.length() - 1); // eliminar última coma
        }

        return json.append("}").toString();
    }
    public static Map<String, String> parseJson(String json) {
        Map<String, String> map = new HashMap<>();

        // Limpiar JSON básico
        json = json.replaceAll("\\s+", ""); // eliminar espacios
        json = json.replace("{", "").replace("}", "");
        String[] pairs = json.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2); // dividir en clave y valor
            if (keyValue.length == 2) {
                String key = clean(keyValue[0]);
                String value = clean(keyValue[1]);
                map.put(key, value);
            }
        }

        return map;
    }

    public static String clean(String s) {
        return s.replaceAll("\"", "").trim(); // limpiar comillas
    }
    public static String extractQueryFromJson(String json) {
        // Busca el campo "query" en el JSON manualmente
        String targetKey = "\"query\":";
        int start = json.indexOf(targetKey);

        if (start == -1) return null;

        start += targetKey.length(); // posición después de "query":
        int end = findClosingQuote(json, start);

        if (end == -1) return null;

        return json.substring(start, end).trim();
    }

    public static int findClosingQuote(String json, int startPos) {
        boolean insideQuotes = false;
        for (int i = startPos; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '"') insideQuotes = !insideQuotes;
            if (c == ',' && !insideQuotes) return i;
            if (c == '}') return i;
        }
        return json.indexOf("}", startPos);
    }

}
