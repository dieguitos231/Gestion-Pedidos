package co.sgp.Models.Pedido;


public class Pedido {
    private static  Integer contador=1;
    private Integer id;
    private final Long productoId;
    private final Integer NIT;
    private final String nombreCliente;
    private final Prioridad prioridad;
    private Estado estado;
    private final Integer cantidad;


    public Pedido(Long productoId, Integer NIT, String nombreCliente, Prioridad prioridad, Integer cantidad) {
        this.productoId = productoId;
        this.NIT = NIT;
        this.nombreCliente = nombreCliente;
        this.prioridad = prioridad;
        this.estado = Estado.PENDIENTE;
        this.cantidad = cantidad;
    }

    public static Integer generarSiguienteId() {
        return contador++;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public Integer getNIT() {
        return NIT;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Integer getCantidad() {
        return cantidad;
    }
}

