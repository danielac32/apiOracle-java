package project.utils;

import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlFileLoader {

    // Directorio base donde se encuentran los archivos SQL
    private static final String SQL_DIRECTORY = Paths.get( "sql").toString();

    /**
     * Carga un archivo SQL y reemplaza los parámetros de fecha
     *
     * @param filename Nombre del archivo SQL (ej. "pagadas.sql")
     * @param desde Fecha desde en formato Date
     * @param hasta Fecha hasta en formato Date
     * @return String con el contenido del SQL con los parámetros reemplazados
     * @throws IOException Si hay error al leer el archivo
     */
    public static String loadFile(String filename, Date desde, Date hasta) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío");
        }
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        if (desde.after(hasta)) {
            throw new IllegalArgumentException("La fecha desde no puede ser posterior a la fecha hasta");
        }

        // Ruta completa del archivo
        Path path = Paths.get(SQL_DIRECTORY, filename);

        if (!Files.exists(path)) {
            throw new IOException("El archivo " + path.toString() + " no existe");
        }

        String content = new String(Files.readAllBytes(path));

        // Formatear fechas
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String strDesde = sdf.format(desde);
        String strHasta = sdf.format(hasta);

        // Reemplazar parámetros en consulta
        content = content.replace("TO_DATE(:PAR_DESDE,'DD/MM/RRRR')", "TO_DATE('" + strDesde + "','DD/MM/YYYY')")
                .replace("TO_DATE(:PAR_HASTA,'DD/MM/RRRR')", "TO_DATE('" + strHasta + "','DD/MM/YYYY')")
                .replace(":PAR_DESDE", "'" + strDesde + "'")
                .replace(":PAR_HASTA", "'" + strHasta + "'");

        return content;
    }

    /**
     * Versión sobrecargada que acepta fechas como Strings en formato dd/MM/yyyy
     */
    public static String loadFile(String filename, String strDesde, String strHasta) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date desde = sdf.parse(strDesde);
        Date hasta = sdf.parse(strHasta);
        return loadFile(filename, desde, hasta);
    }
}