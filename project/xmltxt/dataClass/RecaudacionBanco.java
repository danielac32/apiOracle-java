package project.xmltxt.dataClass;

import java.util.Date;

public class RecaudacionBanco {
    private static final long serialVersionUID = 8355287749132654347L;
    private String banco;
    private Date fechaRecaudacion;
    private short estatus;

    public RecaudacionBanco() {
        super();
        // TODO Auto-generated constructor stub
    }

    public RecaudacionBanco(String banco, Date fechaRecaudacion) {
        super();
        this.banco = banco;
        this.fechaRecaudacion = fechaRecaudacion;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Date getFechaRecaudacion() {
        return fechaRecaudacion;
    }

    public void setFechaRecaudacion(Date fechaRecaudacion) {
        this.fechaRecaudacion = fechaRecaudacion;
    }

    public short getEstatus() {
        return estatus;
    }

    public void setEstatus(short estatus) {
        this.estatus = estatus;
    }
}
