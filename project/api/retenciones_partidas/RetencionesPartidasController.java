package project.api.retenciones_partidas;

import com.sun.net.httpserver.HttpExchange;
import project.db.OracleDb;
import project.utils.QueryParser;
import project.utils.ResponseUtils;
import project.utils.SqlFileLoader;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RetencionesPartidasController {
    public void execute(HttpExchange exchange) throws IOException {
        try {
            String uriQuery = exchange.getRequestURI().getQuery();
            Map<String, String> params = QueryParser.parseQueryParams(uriQuery);

            String desde = params.get("desde");
            String hasta = params.get("hasta");

            if(desde == null || hasta == null){
                Map<String, ?> response = Map.of(
                        "susses", false,
                        "msg", "queryparams no proporcionado"
                );

                ResponseUtils.respuestaJSON(exchange, 200, response);
            }

            String sql = SqlFileLoader.loadFile("retenciones_partidas.sql", desde, hasta);
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
