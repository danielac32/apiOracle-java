package project.xmltxt.process;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.sun.net.httpserver.HttpExchange;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import project.db.OracleDb;
import project.utils.ResponseUtils;
import project.xmltxt.dataClass.TxtSeniat;
import project.xmltxt.dataClass.XmlSeniat;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import  java.util.logging.*;

public class Process {

    public static String extraerYFormatearFecha(String nombreArchivo) {
        // Expresión regular para encontrar el patrón _YYYYMMDD_
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("_(\\d{8})_");
        java.util.regex.Matcher matcher = pattern.matcher(nombreArchivo);

        if (matcher.find()) {
            String fechaStr = matcher.group(1);
            try {
                // Parsear día, mes y año
                String año = fechaStr.substring(0, 4);
                String mes = fechaStr.substring(4, 6);
                String dia = fechaStr.substring(6, 8);

                // Validar valores básicos
                int mesInt = Integer.parseInt(mes);
                int diaInt = Integer.parseInt(dia);

                if (mesInt < 1 || mesInt > 12 || diaInt < 1 || diaInt > 31) {
                    return "[Fecha inválida]";
                }

                return String.format("%s/%s/%s", dia, mes, año); // Formato DD/MM/YYYY
            } catch (Exception e) {
                return "[Error fecha]";
            }
        }
        return "[Sin fecha]";
    }

    public static String formatFileSize(long sizeBytes) {
        if (sizeBytes < 1024) return sizeBytes + " B";
        if (sizeBytes < 1024 * 1024) return String.format("%.2f KB", sizeBytes / 1024.0);
        return String.format("%.2f MB", sizeBytes / (1024.0 * 1024.0));
    }
    public void list(HttpExchange exchange, String rutaDirectorio) throws IOException {
        File directorio = new File(rutaDirectorio);

        if (!directorio.exists() || !directorio.isDirectory()) {
            System.err.println("La ruta proporcionada no es un directorio válido.");
            ResponseUtils.respuestaJSON(exchange, 400, Map.of("error", "Ruta inválida"));
            return;
        }

        File[] archivos = directorio.listFiles((dir, nombre) -> nombre.toLowerCase().endsWith(".xml"));

        if (archivos == null) {
            archivos = new File[0]; // Evitar null si no hay archivos
        }

        // Convertir File[] a List<String> con los nombres de los archivos
        List<String> nombresArchivos = new ArrayList<>();
        for (File archivo : archivos) {
            String nombreArchivo = archivo.getName();
            String fechaFormateada = extraerYFormatearFecha(nombreArchivo);
            String tamañoFormateado = formatFileSize(archivo.length());

            String nombreCompleto = String.format("%s %s %s",
                    nombreArchivo,
                    fechaFormateada,
                    tamañoFormateado);

            nombresArchivos.add(nombreCompleto);
        }

        // Crear respuesta estructurada
        Map<String, Object> respuestaFinal = new HashMap<>();
        respuestaFinal.put("size", nombresArchivos.size());
        respuestaFinal.put("files", nombresArchivos);

        ResponseUtils.respuestaJSON(exchange, 200, Map.of("res", respuestaFinal));
    }
    public void procesarDirectorio(HttpExchange exchange, String rutaDirectorio) throws IOException {
        try {
            File directorio = new File(rutaDirectorio);

            if (!directorio.exists() || !directorio.isDirectory()) {
                System.err.println("La ruta proporcionada no es un directorio válido.");
                ResponseUtils.respuestaJSON(exchange, 400, Map.of("error", "Ruta inválida"));
                return;
            }

            File[] archivos = directorio.listFiles((dir, nombre) -> nombre.toLowerCase().endsWith(".xml"));

            if (archivos == null || archivos.length == 0) {
                System.out.println("⚠️ No se encontraron archivos XML.");
                ResponseUtils.respuestaJSON(exchange, 200, Map.of("mensaje", "No hay archivos XML para procesar."));
                return;
            }

            OracleDb db = new OracleDb();
            db.connect("config2");

            List<Map<String, Object>> resultados = new ArrayList<>();
            int totalPlanillas = 0;
            int totalErrores = 0;

            for (File archivo : archivos) {
                try {
                    System.out.println("🔍 Procesando archivo: " + archivo.getName());
                    Map<String, Integer> resultado = processXml(db, archivo);

                    int planillas = resultado.getOrDefault("planillas", 0);
                    int errores = resultado.getOrDefault("errores", 0);

                    totalPlanillas += planillas;
                    totalErrores += errores;

                    Map<String, Object> infoArchivo = new HashMap<>();
                    infoArchivo.put("archivo", archivo.getName());
                    infoArchivo.put("planillas", planillas);
                    infoArchivo.put("errores", errores);

                    resultados.add(infoArchivo);
                    archivo.delete();
                } catch (Exception e) {
                    System.err.println("❌ Error crítico con archivo " + archivo.getName() + ": " + e.getMessage());
                    totalErrores++;

                    Map<String, Object> errorInfo = new HashMap<>();
                    errorInfo.put("archivo", archivo.getName());
                    errorInfo.put("planillas", 0);
                    errorInfo.put("errores", 1);
                    errorInfo.put("detalle", e.getMessage());

                    resultados.add(errorInfo);
                }
            }

            db.close();

            // Respuesta final
            Map<String, Object> respuestaFinal = new HashMap<>();
            respuestaFinal.put("resultados", resultados);
            respuestaFinal.put("total_planillas", totalPlanillas);
            respuestaFinal.put("total_errores", totalErrores);

            ResponseUtils.respuestaJSON(exchange, 200, respuestaFinal);

        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.respuestaJSON(exchange, 500, Map.of("error", e.getMessage()));
        }
    }

    private Map<String, Integer> processXml(OracleDb db,File archivo) {
        int planillasProcesadas = 0;
        int errores = 0;

        Map<String, Integer> status = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(archivo);

            NodeList nodosPlanilla = documento.getElementsByTagName("Planilla_Pago");
            XmlSeniat xml = null;
            TxtSeniat t = null;

            for (int i = 0; i < nodosPlanilla.getLength(); i++) {
                Node nodo = nodosPlanilla.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodo;
                    xml = armarCabeceraPlanilla(elemento, new XmlSeniat());
                    if (!xml.getBanco().equals("999") && !xml.getBanco().equals("000") && !xml.getForma().equals("79084") && !xml.getForma().equals("99084") && !xml.getForma().equals("00084")){
                           // System.out.println(xml.toString());
                        t = crearTxt(xml);
                       // System.out.println(t.toString());
                        try {
                            insertarTxtSeniat(t, db);
                            planillasProcesadas++;
                        } catch (Exception e) {
                            errores++;
                            System.out.println("❌ Error al insertar en BD - Archivo: " + archivo.getName());
                            System.out.println("   - Causa: " + e.getMessage());
                            System.out.println("   - Datos: " + t.toString());
                            System.out.println("----------------------------------");
                        }
                    }else{

                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error al procesar el archivo " + archivo.getName() + ": " + e.getMessage());
            errores++;
            //System.err.println("❌ Error en planilla " + i + " del archivo " + archivo.getName() + ": " + e.getMessage());
        }
        status.put("planillas", planillasProcesadas);
        status.put("errores", errores);
        return status;
    }

    /*private String getValorElemento(Element elemento, String nombreEtiqueta) {
        NodeList nodos = elemento.getElementsByTagName(nombreEtiqueta);
        if (nodos.getLength() > 0) {
            return nodos.item(0).getTextContent();
        }
        return "";
    }*/

    private static XmlSeniat armarCabeceraPlanilla(Element element,XmlSeniat xml) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        xml.setBanco(element.getElementsByTagName("Cod_Banco").item(0).getTextContent());
        xml.setAgencia(element.getElementsByTagName("Cod_Agencia").item(0).getTextContent());
        xml.setSafe(element.getElementsByTagName("Cod_Safe").item(0).getTextContent());
        xml.setSeguridad(element.getElementsByTagName("Cod_Seguridad_Planilla").item(0).getTextContent());
        xml.setFechaTransmision(sdf.parse(element.getElementsByTagName("Fe_Transmision").item(0).getTextContent()));
        xml.setFechaRecaudacion(sdf.parse(element.getElementsByTagName("Fe_Recaudacion").item(0).getTextContent()));
        xml.setRif(element.getElementsByTagName("Rif_Contribuyente").item(0).getTextContent());
        xml.setForma(element.getElementsByTagName("Cod_Forma").item(0).getTextContent());
        xml.setPlanilla(element.getElementsByTagName("Num_Planilla").item(0).getTextContent());
        xml.setPeriodo(element.getElementsByTagName("Periodo").item(0).getTextContent());
        xml.setAduana(element.getElementsByTagName("Cod_Aduana").item(0).getTextContent());
        xml.setRegion(element.getElementsByTagName("Cod_Region").item(0).getTextContent());
        xml.setElectronico(element.getElementsByTagName("Cancelado_Electronicamente").item(0).getTextContent());
        xml.setMonto(Double.valueOf(element.getElementsByTagName("Monto_Total_Transmision").item(0).getTextContent().replace(",",".")));
        xml.setEfectivo(Double.valueOf(element.getElementsByTagName("Monto_Efectivo_Total").item(0).getTextContent().replace(",",".")));
        xml.setCheque(Double.valueOf(element.getElementsByTagName("Monto_Cheque_Total").item(0).getTextContent().replace(",",".")));
        xml.setTitulo(0.0);
        xml.setCert(Double.valueOf(element.getElementsByTagName("Monto_Cert_Total").item(0).getTextContent().replace(",",".")));
        xml.setBono(Double.valueOf(element.getElementsByTagName("Monto_Bonos_Export_Total").item(0).getTextContent().replace(",",".")));
        xml.setDpn(Double.valueOf(element.getElementsByTagName("Monto_Bonos_DPN_Total").item(0).getTextContent().replace(",",".")));
        return xml;
    }

    public static TxtSeniat crearTxt(XmlSeniat xml) throws Exception {
        TxtSeniat txt = new TxtSeniat();

        String organismo;
        String banco;
        String agencia;
        String rif;



        //1.- Procesando el Organismo
        organismo = xml.getForma().substring(0,2);
        if (organismo.equals("15"))
            organismo = "26";
        else if (organismo.equals("99"))
            organismo = "00";
        else if (organismo.equals("31"))
            organismo = "49";

        txt.setOrganismo(organismo);

        //2.- Procesando el Banco
        banco = xml.getBanco();

        if (banco.substring(0, 1).equals("9"))
            banco = "9" + banco;

        txt.setBanco(banco);

        //3.- Procesando la Agencia
        agencia = xml.getAgencia();
        if (banco.equals("108") && agencia.substring(0, 1).equals("4"))
            agencia = "2" + agencia;
        else
            agencia = "0" + agencia;

        txt.setAgencia(agencia);

        //4.- Procesando el Rif
        rif = xml.getRif();
        String numero_rif = rif.substring(1);

        if (rif.substring(0, 1).equals("1"))
            rif = "V" + numero_rif;
        else if (rif.substring(0, 1).equals("2"))
            rif = "E" + numero_rif;
        else if (rif.substring(0, 1).equals("3"))
            rif = "J" + numero_rif;
        else if (rif.substring(0, 1).equals("4"))
            rif = "P" + numero_rif;
        else if (rif.substring(0, 1).equals("5"))
            rif = "G" + numero_rif;

        txt.setRif(rif);

        //5.- Procesando el Planilla
        txt.setPlanilla(xml.getPlanilla());

        //6.- Procesando la Fecha de Recaudacion
        txt.setFechaRecaudacion(xml.getFechaRecaudacion());

        //7.- Procesando el Tipo de Transaccion
        txt.setTipoTransaccion(2);

        //8.- Procesando la Forma
        txt.setForma(xml.getForma());

        //9.- Procesando el Efectivo y Cheque
        txt.setEfectivo(xml.getEfectivo() + xml.getCheque());

        //10.-Procesando Otros Pagos
        txt.setOtrosPagos(xml.getBono()+xml.getCert()+xml.getDpn());

        //11.-Procesando el Cod_Seguridad
        txt.setSeguridad(xml.getSeguridad());

        //12.-Procesando el Cod_Safe
        txt.setSafe(xml.getSafe());

        txt.setEstado(0);       // Por ejemplo, 0 si no hay estado definido
        txt.setAnho(0);      // Año actual o extraer de la fecha
        txt.setLoteSeq(0);      // 0 si no hay secuencia de lote
        txt.setPlanSeq(0);      // 0 si no hay secuencia de planilla

        return txt;
    }

    public void insertarTxtSeniat(TxtSeniat t, OracleDb db) {
            String sqlInsert = "INSERT INTO TXT_SENIAT (" +
                    "ORGA_ID, INFN_CODIGO, AGENCIA_CODIGO, IDENT_CNTB, PLANILLA, FECHA_RECAUDACION, " +
                    "TIPO_TRANSACCION, FORMA_CODIGO, MONTO_EFECTIVO, MONTO_OTROS_PAGOS, COD_SEGURIDAD, " +
                    "SAFE, ESTADO, ANHO, LOTE_SEQ, PLAN_SEQ" +
                    ") VALUES (?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFormateada = t.getFechaRecaudacion() != null ?
                    sdf.format(t.getFechaRecaudacion()) : null;

            List<Object> params = Arrays.asList(
                    t.getOrganismo(),
                    t.getBanco(),
                    t.getAgencia(),
                    t.getRif(),
                    t.getPlanilla(),
                    fechaFormateada,  // Fecha como String en formato DD/MM/YYYY
                    t.getTipoTransaccion(),
                    t.getForma(),
                    t.getEfectivo(),
                    t.getOtrosPagos(),
                    t.getSeguridad(),
                    t.getSafe(),
                    t.getEstado(),
                    t.getAnho(),
                    t.getLoteSeq(),
                    t.getPlanSeq()
            );

            db.insert(sqlInsert, params);

    }

}
