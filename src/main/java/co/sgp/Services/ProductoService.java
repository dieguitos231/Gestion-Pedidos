package co.sgp.Services;
import co.sgp.Models.Producto.Producto;
import co.sgp.Repository.ProductoRepository;
import co.sgp.Utils.Validador;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ProductoService {

    // Dependencia para llamar todos los métodos de productoRepository
    private final ProductoRepository productoRepository;
    // Constructor para inyectar productoRepository
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    //Crear Producto
    public Producto crearProducto(Producto producto) {
        //Validación de campos
        if (producto.getNombreProducto() == null) {
            throw new IllegalArgumentException("Campo nombreProducto obligatorio");
        }
        if (producto.getStock() == null) {
            throw new IllegalArgumentException("Campo stock obligatorio");
        }

        //Validación de campos digitados
        if (!Validador.esTextoValido(producto.getNombreProducto())) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío y solo debe contener letras");
        }

        if (!Validador.esNumeroPositivo(producto.getStock())){
            throw new IllegalArgumentException("El stock no puede ser menor o igual a 0.");
        }

        productoRepository.save(producto);
        return producto;
    }

    //Actualizar stock de un producto
    public Producto actualizarStock(Long productoId,Integer nuevoStock) {
        Producto producto = buscarProducto(productoId);
        if (producto != null) {
            Integer stockActual = producto.getStock();
            if (nuevoStock < 0){
                throw new IllegalArgumentException("El stock no puede ser negativo.");
            }
            if (stockActual.equals(nuevoStock)) {
                throw new IllegalArgumentException("No se actualizo el stock porque tiene la misma cantidad al stock actual");
            }
            producto.setStock(nuevoStock);
            productoRepository.save(producto);
            return producto;
        }
        throw new NoSuchElementException("No existe el producto con el id: " + productoId);
    }

    //Lista de productos
    public List<Producto>listadoProductos() {
        return productoRepository.findAll();
    }

    //Buscar producto por ID
    public Producto buscarProducto(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new NoSuchElementException("No existe el pedido con ID : "+productoId);
        }
        return productoRepository.findById(productoId).orElse(null);
    }

    //Buscar producto por nombre
    public List<Producto> busquedaPorNombre(String nombre) {
        return productoRepository.findBynombreProductoContainingIgnoreCase(nombre);
    }

    //Buscar productos con un stock menor al valorDigitado
    public List<Producto> buscarProductosStockMenorAValor(Integer valor) {
        if (!Validador.esNumeroPositivo(valor)) {
            throw new IllegalArgumentException("No se puede filtrar por un numero menor o igual a 0.");
        }
        return productoRepository.findBystockLessThan(valor);
    }

    //Lista de productos con stock agotado
    public List<Producto> listaProductosAgotados() {
        return productoRepository.findBystock(0);
    }

    //Eliminar producto
    public void eliminarProducto(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new NoSuchElementException("No existe el pedido con ID : "+productoId);
        }
        productoRepository.deleteById(productoId);
    }
}
