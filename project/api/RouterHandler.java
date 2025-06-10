package project.api;



import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
//import project.ApiController;
import project.ResponseUtils;
import project.api.pagada_retencion.RetencionController;
import project.api.pagadas.PagadasController;
import project.api.pendientes.PendientesController;

import java.io.IOException;
import java.util.Map;

public class RouterHandler implements HttpHandler {
    //private final ApiController controller = new ApiController();
     private final RetencionController retenciones = new RetencionController();
     private final PagadasController pagadas = new PagadasController();
     private final PendientesController pendientes = new PendientesController();


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath(); // "/api/query/pagadas"

        if (!"POST".equalsIgnoreCase(method)) {
            ResponseUtils.respuestaJSON(exchange, 405, Map.of("error", "Método no permitido"));
            return;
        }

        switch (path) {
            case "/api/query/pagadas":
                pagadas.execute(exchange);
                break;
            case "/api/query/pagadas-retenciones":
                retenciones.execute(exchange);
                break;
            case "/api/query/pendientes":
                pendientes.execute(exchange);
                break;
            default:
                ResponseUtils.respuestaJSON(exchange, 404, Map.of("error", "Ruta no encontrada: " + path));
                break;
        }
    }
}