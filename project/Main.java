package project;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.StandardCharsets;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.*;
import project.api.RouterHandler;
import project.xmltxt.XmlRoute;
import project.xmltxt.process.Process;

public class Main {

    public static void main(String[] args) throws IOException {
        int port = 8000;

        /*String rawQuery = "SELECT p.anho presupuesto, " +
                "o.orga_id||' '||o.denominacion organismo, " +
                "pi.unad_id cod_unidad_administradora, " +
                "ua.denominacion desc_unidad_administradora, " +
                "p.pago_id orden, " +
                "DECODE(pi.autorizado_id, NULL, pi.nombre_benef||' '||pi.apellido_benef, pi.nombre_autori||' '||pi.apellido_autori) beneficiario, " +
                "r.RETE_ID||'-'||rt.DENOMINACION retencion, " +
                "DECODE(pi.autorizado_id, NULL, pi.ident_benef, pi.ident_autori) rif, " +
                "pi.monto_bruto monto_orden_ant, " +
                "DECODE(p.anho, 2007, ROUND((pi.monto_bruto/1000),2), 2006, ROUND((pi.monto_bruto/1000),2), pi.monto_bruto) monto_orden, " +
                "r.monto monto_1_x_500_ant, " +
                "DECODE(p.anho, 2007, ROUND((r.monto/1000),2), 2006, ROUND((r.monto/1000),2), r.monto) monto_1_x_500, " +
                "p.fecha_pago " +
                "FROM pago p, pago_imputado pi, retencion_x_pago r, organismo o, retencion rt, unidad_administradora ua " +
                "WHERE p.anho = pi.anho " +
                "AND p.orga_id = pi.orga_id " +
                "AND p.pago_id = pi.pago_id " +
                "AND p.tipg_id = pi.tipg_id " +
                "AND p.anho = r.anho " +
                "AND p.orga_id = r.orga_id " +
                "AND p.pago_id = r.pago_id " +
                "AND p.tipg_id = r.tipg_id " +
                "AND p.orga_id = o.orga_id " +
                "AND R.RETE_ID = Rt.RETE_ID " +
                "AND R.ORGA_ID = Rt.ORGA_ID " +
                "AND R.TIRE_ID = Rt.TIRE_ID " +
                "AND ua.unad_id = pi.unad_id " +
                "AND ua.anho = pi.anho " +
                "AND ua.orga_id = pi.orga_id " +
                "AND p.tipg_id IN (1,10) " +
                "AND p.razon_id_fin IN (4,10,11,12,13) " +
                "AND TO_DATE(fecha_pago,'DD/MM/RRRR') BETWEEN TO_DATE('01/05/2025','DD/MM/RRRR') AND TO_DATE('10/06/2025','DD/MM/RRRR') " +
                "ORDER BY 1,2,3 ASC";


         try{
            OracleDb db = new OracleDb();
            db.connect("config1");
             List<Map<String, Object>> result = db.executeQuery(rawQuery);


             for (Map<String, Object> row : result) {
                 System.out.println("Orden: " + row.get("ORDEN") +
                         ", Beneficiario: " + row.get("BENEFICIARIO") +
                         ", Monto: " + row.get("MONTO"));
             }
            db.close();

        }catch (Exception ex) {
            System.out.println(ex);
        }*/


       // Process process = new Process();
       // process.procesarDirectorio(null,"xml");

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("✅ Servidor corriendo en http://localhost:" + port);


        server.createContext("/api/query", new RouterHandler());//ruta para consultar ordenes
        server.createContext("/api/xmltxt", new XmlRoute());//ruta para subir xml

        /*server.createContext("/api/query/pagadas-retenciones", new RetencionesController());
        server.createContext("/api/query/pagadas", new PagadasController());
        server.createContext("/api/query/pendientes", new PendientesController());*/


        server.setExecutor(null); // usar hilo por defecto
        server.start();
        System.out.println("Servidor listo...");



        /*
        int puerto = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(puerto), 0);
        System.out.println("✅ Servidor corriendo en http://localhost:" + puerto);
        // Ruta /api
        server.createContext("/api", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String metodo = exchange.getRequestMethod();
                try {
                    if (metodo.equalsIgnoreCase("GET")) {
                        manejarGET(exchange);
                    } else if (metodo.equalsIgnoreCase("POST")) {
                        manejarPOST(exchange);
                    } else if (metodo.equalsIgnoreCase("PUT")) {
                        manejarPUT(exchange);
                    } else if (metodo.equalsIgnoreCase("PATCH")) {
                        manejarPATCH(exchange);
                    } else if (metodo.equalsIgnoreCase("DELETE")) {
                        manejarDELETE(exchange);
                    } else {
                        respuestaJSON(exchange, 405, "Método no permitido");
                    }
                } catch (IOException e) {
                    respuestaJSON(exchange, 500, "Error interno");
                }
            }
        });

        server.setExecutor(null); // usar executor por defecto
        server.start();
        System.out.println("Servidor listo...");*/

    }
/*
    private static void manejarGET(HttpExchange exchange) throws IOException {
        String respuesta = "{\"mensaje\": \"Este es un GET\"}";
        respuestaJSON(exchange, 200, respuesta);
    }

    private static void manejarPOST(HttpExchange exchange) throws IOException {
        String cuerpo = leerCuerpo(exchange);
        String respuesta = "{\"mensaje\": \"Este es un POST\", \"datos\": " + cuerpo + "}";
        respuestaJSON(exchange, 200, respuesta);
    }

    private static void manejarPUT(HttpExchange exchange) throws IOException {
        String cuerpo = leerCuerpo(exchange);
        String respuesta = "{\"mensaje\": \"Este es un PUT\", \"datos\": " + cuerpo + "}";
        respuestaJSON(exchange, 200, respuesta);
    }

    private static void manejarPATCH(HttpExchange exchange) throws IOException {
        String cuerpo = leerCuerpo(exchange);
        String respuesta = "{\"mensaje\": \"Este es un PATCH\", \"datos\": " + cuerpo + "}";
        respuestaJSON(exchange, 200, respuesta);
    }

    private static void manejarDELETE(HttpExchange exchange) throws IOException {
        String respuesta = "{\"mensaje\": \"Este es un DELETE\"}";
        respuestaJSON(exchange, 200, respuesta);
    }

    // Métodos auxiliares

    private static void respuestaJSON(HttpExchange exchange, int codigo, String mensaje) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = mensaje.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(codigo, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String leerCuerpo(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        StringBuilder texto = new StringBuilder();
        int b;

        while ((b = is.read()) != -1) {
            texto.append((char) b);
        }

        return texto.toString().isEmpty() ? "{}" : texto.toString();
    }*/


}




















 /*try{
            OracleDb db = new OracleDb();
            db.connect("config1");

            db.close();
        }catch (Exception ex) {
            System.out.println(ex);
        }*/