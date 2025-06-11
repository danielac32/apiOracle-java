package project.api;



import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
//import project.ApiController;

import project.api.ordenesDivisaBolivares.DivisasBolivares;
import project.api.ordenesPendientes.Pendientes2;
import project.api.pagada_retencion.RetencionController;
import project.api.pagadas.PagadasController;
import project.api.pagadas2.Pagadas2;
import project.api.pendientes.PendientesController;
import project.api.trasmiciones.Transmisiones;
import project.utils.ResponseUtils;

import java.io.IOException;
import java.util.Map;

public class RouterHandler implements HttpHandler {
    //private final ApiController controller = new ApiController();
     private final RetencionController retenciones = new RetencionController();
     private final PagadasController pagadas = new PagadasController();
     private final PendientesController pendientes = new PendientesController();
     private final Transmisiones transmisiones = new Transmisiones();
     private  final Pagadas2 pagadas2 = new Pagadas2();

     private final Pendientes2 pendientes2 = new Pendientes2();

     private final DivisasBolivares divisasBolivares = new DivisasBolivares();

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
            case "/api/query/transmisiones":
                transmisiones.execute(exchange);
                break;
            case "/api/query/pagadas2":
                pagadas2.execute(exchange);
                break;
            case "/api/query/ordenes-pendientes"://con beneficiario
                pendientes2.execute(exchange);
                break;
            case "/api/query/ordenes-divisas-bolivares":
                divisasBolivares.execute(exchange);
                break;
            default:
                ResponseUtils.respuestaJSON(exchange, 404, Map.of("error", "Ruta no encontrada: " + path));
                break;
        }
    }
}