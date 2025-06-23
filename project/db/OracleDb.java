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
            //System.exit(0);
        }
    }


    public void insert(String query, List<Object> params) {
        if (connection == null) {
            System.err.println("Primero debes establecer la conexión");
            return;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Validar parámetros
            int expectedParams = query.replaceAll("[^?]", "").length();
            if (params.size() != expectedParams) {
                throw new SQLException("Número incorrecto de parámetros. Esperados: " +
                        expectedParams + ", Recibidos: " + params.size());
            }

            // Asignar parámetros con logging
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                System.out.printf("Param %d: %s (%s)%n",
                        i+1,
                        param != null ? param.toString() : "NULL",
                        param != null ? param.getClass().getSimpleName() : "NULL");
                pstmt.setObject(i + 1, param);
            }

            int rowsAffected = pstmt.executeUpdate();
            //System.out.println("✅ Filas afectadas: " + rowsAffected);
            connection.commit();  // Asegurar el commit

        } catch (SQLException e) {
            System.err.println("❌ Error en INSERT: " + e.getMessage());
            try {
                connection.rollback();  // Revertir en caso de error
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }

            // Información adicional del error
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
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
    public static void describeTable(OracleDb db) {
        String query = "SELECT column_name, data_type, data_length FROM all_tab_columns " +
                "WHERE table_name = 'TXT_SENIAT' ORDER BY column_id";

        try {
            System.out.println("=== Estructura de TXT_SENIAT ===");
            System.out.println("Columna\t\tTipo\t\tLongitud");
            System.out.println("----------------------------------");

            ResultSet rs = (ResultSet) db.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getString("column_name") + "\t\t" +
                        rs.getString("data_type") + "\t\t" +
                        rs.getInt("data_length"));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error al obtener estructura de la tabla: " + e.getMessage());
        }
    }
    public void executeUpdate(String query, List<Object> params) {
        if (connection == null) {
            System.err.println("Primero debes establecer la conexión");
           // System.exit(0);
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
            //System.exit(0);
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