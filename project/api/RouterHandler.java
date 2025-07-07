package project.api;



import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
//import project.ApiController;

import project.api.ordenesDivisaBolivares.DivisasBolivares;
import project.api.ordenesPendientes.Pendientes2;
import project.api.pagada_retencion.RetencionController;
import project.api.pagadas.PagadasController;
import project.api.pagadas2.Pagadas2;
import project.api.pendientes.PendientesController;
import project.api.retenciones_partidas.RetencionesPartidasController;
import project.api.trasmiciones.Transmisiones;
import project.db.OracleDb;
import project.utils.ResponseUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
     private final RetencionesPartidasController retencionesPartidas = new RetencionesPartidasController();

     /*
     TRANSMISION DE ORDENES( ORDENES TRANSMITIDAS BCV)

    PAGADAS DARCHY (ORDENES PAGADAS)

    ORDENES PENDIENTES CON BENEFICIARIO (ORDENES PENDIENTES)

    DOLARES A BOLIVARES (ORDENES DIVISAS A BOLIVARES)
      */
    public List<String> getAvailableRoutes() {
        return Arrays.asList(
                "pagadas",
                "pagadas-retenciones",
                "pendientes",
                "transmisiones",
                "pagadas2",
                "ordenes-pendientes",
                "ordenes-divisas-bolivares"
        );
    }

/*
    public List<RouteOption> getAvailableRoutes() {
        return Arrays.asList(
                new RouteOption("pagadas", "PAGADAS", "Consulta de órdenes pagadas"),
                new RouteOption("pagadas-retenciones", "PAGADAS CON RETENCIONES", "Consulta de órdenes con retenciones"),
                new RouteOption("pendientes", "PENDIENTES", "Consulta de órdenes pendientes"),
                new RouteOption("transmisiones", "ORDENES TRANSMITIDAS BCV", "Consulta de transmisiones"),
                new RouteOption("pagadas", "ORDENES PAGADAS", "Consulta alternativa de pagadas"),
                new RouteOption("ordenes-pendientes", "ORDENES PENDIENTES", "Órdenes pendientes con beneficiario"),
                new RouteOption("ordenes-divisas-bolivares", "ORDENES DIVISAS A BOLIVARES", "Órdenes de divisas en bolívares")
        );
    }

 */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath(); // "/api/query/pagadas"
        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(204, -1); // 204 No Content
            exchange.getResponseBody().close();
            return;
        }

        if ("GET".equalsIgnoreCase(method) && "/api/query/available-reports".equals(path)) {
            ResponseUtils.respuestaJSON(exchange, 200, Map.of("availableReports", getAvailableRoutes()));
            return;
        }

        if ("GET".equalsIgnoreCase(method) && "/api/query/connection".equals(path)) {
            OracleDb db = new OracleDb();
            boolean connectionStatus = false;

            try {
                db.connect("config1");
                // Intentamos hacer una consulta simple para verificar la conexión
                List<Map<String, Object>> result = db.executeQuery("SELECT COUNT(*) TOTAL FROM PAGO WHERE ROWNUM = 1");
                if (result != null && !result.isEmpty()) {
                    connectionStatus = true;
                }
            } catch (Exception e) {
                connectionStatus = false;
            } finally {
                try {
                    db.close();
                } catch (Exception e) {
                    // Ignorar errores al cerrar la conexión
                }
            }
            ResponseUtils.respuestaJSON(exchange, 200, Map.of(
                    "status", connectionStatus
            ));
            return;
        }

        if (!"POST".equalsIgnoreCase(method)) {
            ResponseUtils.respuestaJSON(exchange, 405, Map.of("error", "Método no permitido"));
            return;
        }

        switch (path) {
            case "/api/query/retenciones-partidas":
                retencionesPartidas.execute(exchange);
                break;
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