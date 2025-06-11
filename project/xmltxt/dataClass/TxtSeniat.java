package project.xmltxt.dataClass;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TxtSeniat {
    private static final long serialVersionUID = -3897111880847596802L;
    private String organismo;
    private String banco;
    private String agencia;
    private String rif;
    private String planilla;
    private Date fechaRecaudacion;
    private Integer tipoTransaccion;
    private String forma;
    private Double efectivo;
    private Double otrosPagos;
    private String seguridad;
    private String safe;
    private Integer estado;
    private Integer anho;
    private Integer loteSeq;
    private Integer planSeq;

    public TxtSeniat(String banco, String agencia, String rif, String planilla, Date fechaRecaudacion, String forma, Double efectivo,
                     Double otrosPagos, String seguridad, String safe) {
        super();
        this.organismo = forma.substring(0, 2);
        this.banco = banco;
        this.agencia = agencia;
        this.rif = rif;
        this.planilla = planilla;
        this.fechaRecaudacion = fechaRecaudacion;
        this.tipoTransaccion = 2;
        this.forma = forma;
        this.efectivo = efectivo;
        this.otrosPagos = otrosPagos;
        this.seguridad = seguridad;
        this.safe = safe;
    }

    public TxtSeniat() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getOrganismo() {
        return organismo;
    }

    public void setOrganismo(String organismo) {
        this.organismo = organismo;
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


    public Date getFechaRecaudacion() {
        return fechaRecaudacion;
    }

    public void setFechaRecaudacion(Date fechaRecaudacion) {
        this.fechaRecaudacion = fechaRecaudacion;
    }

    public Integer getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(Integer tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
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

    public Double getOtrosPagos() {
        return otrosPagos;
    }

    public void setOtrosPagos(Double otrosPagos) {
        this.otrosPagos = otrosPagos;
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

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getAnho() {
        return anho;
    }

    public void setAnho(Integer anho) {
        this.anho = anho;
    }

    public Integer getLoteSeq() {
        return loteSeq;
    }

    public void setLoteSeq(Integer loteSeq) {
        this.loteSeq = loteSeq;
    }

    public Integer getPlanSeq() {
        return planSeq;
    }

    public void setPlanSeq(Integer planSeq) {
        this.planSeq = planSeq;
    }

    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if (!(obj instanceof TxtSeniat)) {
            return false;
        }

        TxtSeniat that = (TxtSeniat) obj;

        boolean isEquals = this.agencia.equals(that.getAgencia()) && this.banco.equals(that.getBanco())
                && this.tipoTransaccion.equals(that.getTipoTransaccion()) && this.planilla.equals(that.getPlanilla())
                &&  this.safe.equals(that.getSafe()) && this.seguridad.equals(that.getSeguridad());


        return isEquals;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        sb.append("\n--- Datos de Transacción SENIAT ---\n");
        sb.append("Organismo: ").append(organismo != null ? organismo : "").append("\n");
        sb.append("Banco: ").append(banco != null ? banco : "").append("\n");
        sb.append("Agencia: ").append(agencia != null ? agencia : "").append("\n");
        sb.append("RIF Contribuyente: ").append(rif != null ? rif : "").append("\n");
        sb.append("Número Planilla: ").append(planilla != null ? planilla : "").append("\n");
        sb.append("Fecha Recaudación: ").append(fechaRecaudacion != null ? sdf.format(fechaRecaudacion) : "").append("\n");
        sb.append("Tipo Transacción: ").append(tipoTransaccion != null ? tipoTransaccion : "").append("\n");
        sb.append("Forma: ").append(forma != null ? forma : "").append("\n");
        sb.append("Efectivo: ").append(efectivo != null ? efectivo : 0.0).append("\n");
        sb.append("Otros Pagos: ").append(otrosPagos != null ? otrosPagos : 0.0).append("\n");
        sb.append("Seguridad: ").append(seguridad != null ? seguridad : "").append("\n");
        sb.append("Safe: ").append(safe != null ? safe : "").append("\n");
        sb.append("Estado: ").append(estado != null ? estado : "").append("\n");
        sb.append("Año: ").append(anho != null ? anho : "").append("\n");
        sb.append("Secuencia Lote: ").append(loteSeq != null ? loteSeq : "").append("\n");
        sb.append("Secuencia Planilla: ").append(planSeq != null ? planSeq : "").append("\n");

        return sb.toString();
    }
}
