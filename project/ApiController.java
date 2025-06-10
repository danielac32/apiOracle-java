package project;
import com.sun.net.httpserver.HttpExchange;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;



public class ApiController {


    public void pagadas_retenciones_handlePost(HttpExchange exchange) throws IOException {
        try {
            String uriQuery = exchange.getRequestURI().getQuery();
            Map<String, String> params = QueryParser.parseQueryParams(uriQuery);

            String desde = params.get("desde");
            String hasta = params.get("hasta");

            if(desde == null || hasta == null){
                Map<String, ?> response = Map.of(
                        "susses", true,
                        "msg", "queryparams no proporcionado"
                );

                ResponseUtils.respuestaJSON(exchange, 200, response);
            }

            String sql = SqlFileLoader.loadFile("pagadas_retenciones.sql", desde, hasta);
            System.out.println(sql);
            OracleDb db = new OracleDb();
            db.connect("config1");
            List<Map<String, Object>> result = db.executeQuery(sql);
            db.close();
           // System.out.println(result);
            ResponseUtils.respuestaJSON(exchange, 200, result);

        }catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.respuestaJSON(exchange, 500, Map.of("error", e.getMessage()));
        }
    }
}


/* // Simular respuesta
        Map<String, String> response = Map.of(
                "desde", desde != null ? desde : "no proporcionado",
                "hasta", hasta != null ? hasta : "no proporcionado"
        );

        ResponseUtils.respuestaJSON(exchange, 200, response);*/