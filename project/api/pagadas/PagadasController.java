package project.api.pagadas;

import com.sun.net.httpserver.HttpExchange;
import project.db.OracleDb;
import project.utils.QueryParser;
import project.utils.ResponseUtils;
import project.utils.SqlFileLoader;
import project.utils.ThreadConsult;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import java.util.concurrent.CompletableFuture;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.function.Consumer;


/*
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
*/
import java.util.UUID;
/*
class PagadasThread extends Thread {
    private final String desde;
    private final String hasta;
    private final Consumer<List<Map<String, Object>>> callback;

    public PagadasThread(String desde, String hasta, Consumer<List<Map<String, Object>>> callback) {
        this.desde = desde;
        this.hasta = hasta;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            System.out.println("Thread " + Thread.currentThread().getId() + " is running");
            String sql = SqlFileLoader.loadFile("pagadas.sql", desde, hasta);
            System.out.println(sql);
            OracleDb db = new OracleDb();
            db.connect("config1");
            List<Map<String, Object>> result = db.executeQuery(sql);
            db.close();
            // Llamamos al callback con el resultado
            callback.accept(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

 */


public class PagadasController {


    public void execute(HttpExchange exchange) throws IOException {
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

            /*String taskId = UUID.randomUUID().toString();

            // 2. Responder inmediatamente con el ID de tarea
            ResponseUtils.respuestaJSON(exchange, 200, Map.of(
                    "taskId", taskId,
                    "status", "processing"
            ));*/


            /*PagadasThread task = new PagadasThread(desde, hasta, result -> {
                try {
                    ResponseUtils.respuestaJSON(exchange, 200, result);
                } catch (IOException ioEx) {
                    ioEx.printStackTrace();
                    try {
                        ResponseUtils.respuestaJSON(exchange, 500, Map.of("error", "Error al enviar respuesta"));
                    } catch (IOException ignored) {}
                }
            });
            task.start(); */
            ThreadConsult task = new ThreadConsult(desde, hasta,"pagadas.sql", result -> {
                try {
                    ResponseUtils.respuestaJSON(exchange, 200, result);
                } catch (IOException ioEx) {
                    ioEx.printStackTrace();
                    try {
                        ResponseUtils.respuestaJSON(exchange, 500, Map.of("error", "Error al enviar respuesta"));
                    } catch (IOException ignored) {}
                }
            });
            task.start();// Iniciar el hilo


           /* String sql = SqlFileLoader.loadFile("pagadas.sql", desde, hasta);
            System.out.println(sql);
            OracleDb db = new OracleDb();
            db.connect("config1");
            List<Map<String, Object>> result = db.executeQuery(sql);
            db.close();
            //System.out.println(result);
            ResponseUtils.respuestaJSON(exchange, 200, result);*/
        }catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.respuestaJSON(exchange, 500, Map.of("error", e.getMessage()));
        }
    }
/*
    public void GnerateExcel( ){

        try (Workbook workbook = new XSSFWorkbook()) {

            // Create a new sheet named "Data"
            Sheet sheet = workbook.createSheet("Data");

            // Create a header row
            Row headerRow = sheet.createRow(0);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Name");
            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Age");

            // Create data rows
            Row dataRow1 = sheet.createRow(1);
            dataRow1.createCell(0).setCellValue("Alice");
            dataRow1.createCell(1).setCellValue(30);

            Row dataRow2 = sheet.createRow(2);
            dataRow2.createCell(0).setCellValue("Bob");
            dataRow2.createCell(1).setCellValue(25);

            // Write the workbook to a file
            try (FileOutputStream fileOut = new FileOutputStream("example.xlsx")) {
                workbook.write(fileOut);
                System.out.println("Excel file 'example.xlsx' created successfully.");
            } catch (IOException e) {
                System.err.println("Error writing Excel file: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Error creating workbook: " + e.getMessage());
        }


    }

 */
}
