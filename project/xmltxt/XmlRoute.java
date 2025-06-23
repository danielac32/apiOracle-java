package project.xmltxt;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import project.utils.ResponseUtils;

import java.io.IOException;
import java.util.Map;
import project.xmltxt.process.Process;

public class XmlRoute implements HttpHandler {
    private final Process process=new Process();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath(); //
// Manejar solicitud OPTIONS (preflight)
        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(204, -1); // 204 No Content
            exchange.getResponseBody().close();
            return;
        }
        if (!"GET".equalsIgnoreCase(method)) {
            ResponseUtils.respuestaJSON(exchange, 405, Map.of("error", "Método no permitido"));
            return;
        }

        switch (path) {
            case "/api/xmltxt/process":
                process.procesarDirectorio(exchange,"xml");
                break;
            case "/api/xmltxt/list":
                process.list(exchange,"xml");
                break;
            case "/api/xmltxt/deleteP":

                break;
            case "/api/xmltxt/deleteE":

                break;
            case "/api/xmltxt/deleteX":

                break;

            default:
                ResponseUtils.respuestaJSON(exchange, 404, Map.of("error", "Ruta no encontrada: " + path));
                break;
        }
    }
}
