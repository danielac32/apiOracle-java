package project.db;
import java.sql.*;
import java.util.*;

public class OracleDb {
    private Connection connection;

    public void connect(String configName) {
        try {
            Map<String, String> config = DbConfig.getConfig(configName);

            System.out.println("🔌 Conectando a la base de datos...");
            Class.forName("oracle.jdbc.driver.OracleDriver");

            connection = DriverManager.getConnection(
                    config.get("dsn"),
                    config.get("user"),
                    config.get("password")
            );

            System.out.println("✅ Conexión establecida.");
        } catch (Exception e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
            System.exit(0);
        }
    }


    public void insert(String query, List<Object> params) {
        if (connection == null) {
            System.err.println("Primero debes establecer la conexión");
            System.exit(0);
            return;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            int rowsAffected = pstmt.executeUpdate();

            // Opcional: si hay clave generada (como ID autoincrementable)
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al ejecutar el INSERT: " + e.getMessage());
            System.exit(0);
        }
    }


    /*public List<Map<String, Object>> executeQuery(String query, Map<String, String> params) {
        if (connection == null) {
            System.err.println("Primero debes establecer la conexión");
            System.exit(0);
            return Collections.emptyList();
        }

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<Map<String, Object>> results = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }

            return results;

        } catch (SQLException e) {
            System.err.println("❌ Error al ejecutar la consulta: " + e.getMessage());
            System.exit(0);
            return Collections.emptyList();
        }
    }*/
    public List<Map<String, Object>> executeQuery(String query) {
        if (connection == null) {
            System.err.println("Primero debes establecer la conexión");
            System.exit(0);
            return Collections.emptyList();
        }

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<Map<String, Object>> results = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }

            return results;

        } catch (SQLException e) {
            System.err.println("❌ Error al ejecutar la consulta: " + e.getMessage());
            System.exit(0);
            return Collections.emptyList();
        }
    }

    public void executeUpdate(String query, List<Object> params) {
        if (connection == null) {
            System.err.println("Primero debes establecer la conexión");
            System.exit(0);
            return;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            pstmt.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            System.err.println("❌ Error al ejecutar la operación de escritura: " + e.getMessage());
            System.exit(0);
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔌 Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}