package project.api.model;

public class RouteOption {
    private String nombre;
    private String valor;
    private String detalle;

    public RouteOption(String nombre, String valor, String detalle) {
        this.nombre = nombre;
        this.valor = valor;
        this.detalle = detalle;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getValor() { return valor; }
    public String getDetalle() { return detalle; }
}
