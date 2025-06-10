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
            json.append(objectToJson(item)).append(",");
        }
        if (!list.isEmpty()) {
            json.deleteCharAt(json.length() - 1); // Eliminar última coma
        }
        return json.append("]").toString();
    }

    private static String mapToJson(Map<?, ?> map) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":");
            json.append(objectToJson(entry.getValue())).append(",");
        }
        if (!map.isEmpty()) {
            json.deleteCharAt(json.length() - 1); // Eliminar última coma
        }
        return json.append("}").toString();
    }

    private static String objectToJson(Object obj) {
        if (obj == null) {
            return "null";
        } else if (obj instanceof String) {
            return "\"" + escapeJson((String) obj) + "\"";
        } else if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        } else if (obj instanceof Date) {
            return "\"" + obj.toString() + "\"";
        } else if (obj instanceof Map) {
            return mapToJson((Map<?, ?>) obj);
        } else if (obj instanceof List) {
            return listToJson((List<?>) obj);
        } else {
            return "\"" + obj.toString() + "\"";
        }
    }

    private static String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }



}
