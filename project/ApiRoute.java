package project;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.*;



public class ApiRoute implements HttpHandler {
    private final ApiController controller = new ApiController();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            controller.pagadas_retenciones_handlePost(exchange);
        } else {
            ResponseUtils.respuestaJSON(exchange, 405, "Método no permitido");
        }
    }
}