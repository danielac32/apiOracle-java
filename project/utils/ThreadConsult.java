package project.utils;

import project.db.OracleDb;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ThreadConsult extends Thread{

    private final String desde;
    private final String hasta;
    private final String filename;

    private final Consumer<List<Map<String, Object>>> callback;

    public ThreadConsult(String desde, String hasta, String filename, Consumer<List<Map<String, Object>>> callback) {
        this.desde = desde;
        this.hasta = hasta;
        this.filename = filename;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            System.out.println("Thread " + Thread.currentThread().getId() + " is running");
            String sql = SqlFileLoader.loadFile(filename, desde, hasta);
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
