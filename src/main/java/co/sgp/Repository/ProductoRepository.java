package co.sgp.Repository;

import co.sgp.Models.Producto.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto,Long>{
    List<Producto> findBynombreProductoContainingIgnoreCase(String nombreProducto);
    List<Producto> findBystockLessThan(Integer stock);
    List<Producto> findBystock(Integer stock);
}
