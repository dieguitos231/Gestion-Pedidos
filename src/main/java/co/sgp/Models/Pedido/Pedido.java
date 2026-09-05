package co.sgp.Models.Pedido;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pedidoId;
    private Long productoId;
    private Integer Nit;
    private String nombreCliente;
    private Prioridad prioridad;
    private Estado estado;
    private Integer cantidad;

    public Pedido(){}

    public Pedido(Long pedidoId,Long productoId, Integer Nit, String nombreCliente, Prioridad prioridad, Integer cantidad) {
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.Nit = Nit;
        this.nombreCliente = nombreCliente;
        this.prioridad = prioridad;
        this.estado = Estado.PENDIENTE;
        this.cantidad = cantidad;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public Integer getNit() {
        return Nit;
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

