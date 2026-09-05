package co.sgp.Models.Producto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productoId;
    private String nombreProducto;
    private Integer stock;

    public Producto() {}

    public Producto(Long productoId, String nombreProducto, Integer stock) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.stock = stock;
    }

    public Long getProductoId() {return productoId;}

    public String getNombreProducto() {
        return nombreProducto;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

}
