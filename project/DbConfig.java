package project;


import java.util.HashMap;
import java.util.Map;

public class DbConfig {

    // Puedes tener múltiples configuraciones como en tu ejemplo
    public static final Map<String, String> config1 = new HashMap<>();

    static {
        config1.put("user", "Consulta");
        config1.put("password", "pumyra1584");
        config1.put("dsn", "jdbc:oracle:thin:@10.79.6.247:1521/SIGEPROD.oncop.gob.ve");
    }

    public static Map<String, String> getConfig(String name) {
        return switch (name) {
            case "config1" -> config1;
            default -> throw new IllegalArgumentException("Configuración desconocida: " + name);
        };
    }
}