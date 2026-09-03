package co.sgp.Models.Producto;

public class Producto {
    private final Long productoId;
    private final String nombreProducto;
    private Integer stock;

    public Producto(Long productoId, String nombreProducto, Integer stock) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.stock = stock;
    }

    public Long getProductoId() {
        return productoId;
    }

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
