package project.xmltxt.dataClass;
import java.text.SimpleDateFormat;
import java.util.Date;



public class XmlSeniat {
    private static final long serialVersionUID = -1673793401724749472L;
    private String banco;
    private String agencia;
    private String safe;
    private String seguridad;
    private Date fechaTransmision;
    private Date fechaRecaudacion;
    private String rif;
    private String forma;
    private String planilla;
    private String periodo;
    private String aduana;
    private String region;
    private String electronico;
    private Double monto;
    private Double efectivo;
    private Double cheque;
    private Double titulo;
    private Double cert;
    private Double bono;
    private Double dpn;
    private String partida;
    private Double montoPartida;
    private int estatus;

    public XmlSeniat(String banco, String agencia, String rif, String planilla,
                     Date fechaRecaudacion, String forma, Double efectivo,
                     Double otrosPagos, String seguridad, String safe) {
        super();

    }

    public XmlSeniat() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }


    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }


    public String getSeguridad() {
        return seguridad;
    }

    public void setSeguridad(String seguridad) {
        this.seguridad = seguridad;
    }


    public String getSafe() {
        return safe;
    }

    public void setSafe(String safe) {
        this.safe = safe;
    }


    public Date getFechaTransmision() {
        return fechaTransmision;
    }

    public void setFechaTransmision(Date fechaTransmision) {
        this.fechaTransmision = fechaTransmision;
    }


    public Date getFechaRecaudacion() {
        return fechaRecaudacion;
    }

    public void setFechaRecaudacion(Date fechaRecaudacion) {
        this.fechaRecaudacion = fechaRecaudacion;
    }


    public String getRif() {
        return rif;
    }

    public void setRif(String rif) {
        this.rif = rif;
    }


    public String getPlanilla() {
        return planilla;
    }

    public void setPlanilla(String planilla) {
        this.planilla = planilla;
    }


    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }


    public Double getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(Double efectivo) {
        this.efectivo = efectivo;
    }


    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }


    public Double getCheque() {
        return cheque;
    }

    public void setCheque(Double cheque) {
        this.cheque = cheque;
    }


    public Double getTitulo() {
        return titulo;
    }

    public void setTitulo(Double titulo) {
        this.titulo = titulo;
    }


    public Double getCert() {
        return cert;
    }

    public void setCert(Double cert) {
        this.cert = cert;
    }

    public Double getBono() {
        return bono;
    }

    public void setBono(Double bono) {
        this.bono = bono;
    }


    public Double getDpn() {
        return dpn;
    }

    public void setDpn(Double dpn) {
        this.dpn = dpn;
    }


    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }


    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }


    public String getAduana() {
        return aduana;
    }

    public void setAduana(String aduana) {
        this.aduana = aduana;
    }


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    public String getElectronico() {
        return electronico;
    }

    public void setElectronico(String electronico) {
        this.electronico = electronico;
    }


    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }


    public Double getMontoPartida() {
        return montoPartida;
    }

    public void setMontoPartida(Double montoPartida) {
        this.montoPartida = montoPartida;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        sb.append("\n--- Planilla de Pago ---\n");
        sb.append("Banco: ").append(banco != null ? banco : "").append("\n");
        sb.append("Agencia: ").append(agencia != null ? agencia : "").append("\n");
        sb.append("Safe: ").append(safe != null ? safe : "").append("\n");
        sb.append("Seguridad Planilla: ").append(seguridad != null ? seguridad : "").append("\n");
        sb.append("Fecha Transmisión: ").append(fechaTransmision != null ? sdf.format(fechaTransmision) : "").append("\n");
        sb.append("Fecha Recaudación: ").append(fechaRecaudacion != null ? sdf.format(fechaRecaudacion) : "").append("\n");
        sb.append("RIF Contribuyente: ").append(rif != null ? rif : "").append("\n");
        sb.append("Código Forma: ").append(forma != null ? forma : "").append("\n");
        sb.append("Número Planilla: ").append(planilla != null ? planilla : "").append("\n");
        sb.append("Periodo: ").append(periodo != null ? periodo : "").append("\n");
        sb.append("Aduana: ").append(aduana != null ? aduana : "").append("\n");
        sb.append("Región: ").append(region != null ? region : "").append("\n");
        sb.append("Cancelado Electrónicamente: ").append(electronico != null ? electronico : "").append("\n");
        sb.append("Monto Total: ").append(monto != null ? monto : 0.0).append("\n");
        sb.append("Monto Efectivo: ").append(efectivo != null ? efectivo : 0.0).append("\n");
        sb.append("Monto Cheque: ").append(cheque != null ? cheque : 0.0).append("\n");
        sb.append("Monto Títulos: ").append(titulo != null ? titulo : 0.0).append("\n");
        sb.append("Monto Certificados: ").append(cert != null ? cert : 0.0).append("\n");
        sb.append("Monto Bonos Export: ").append(bono != null ? bono : 0.0).append("\n");
        sb.append("Monto Bonos DPN: ").append(dpn != null ? dpn : 0.0).append("\n");
        sb.append("Partida: ").append(partida != null ? partida : "").append("\n");
        sb.append("Monto Partida: ").append(montoPartida != null ? montoPartida : 0.0).append("\n");
        sb.append("Estatus: ").append(estatus).append("\n");

        return sb.toString();
    }
}
