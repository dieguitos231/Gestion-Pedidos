package co.sgp.Services;

import co.sgp.Models.Producto.Producto;
import co.sgp.Utils.Validador;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ProductoService {
    private final List<Producto> productos = new ArrayList<>();
    public ProductoService() {
        productos.add(new Producto(23451L, "Papel Fotográfico", 15));
        productos.add(new Producto(34512L, "Bolígrafo BIC", 30));
        productos.add(new Producto(45123L, "Borrador de Goma", 32));
        productos.add(new Producto(54321L, "Lápiz 2B", 25));
        productos.add(new Producto(65432L, "PortaMinas", 10));
        productos.add(new Producto(87654L, "Marcador Borrable", 14));
        productos.add(new Producto(98765L, "Clips", 60));
        productos.add(new Producto(19876L, "Carpetas", 16));
        productos.add(new Producto(20987L, "sobre Manila", 52));
        productos.add(new Producto(32109L, "Pliego de Cartulina", 27));
    }

    //CREAR PEDIDO
    public void crearProducto(Producto producto) {
        //Validación de campos
        if (producto.getProductoId() == null) {
            throw  new IllegalArgumentException("Campo productoId obligatorio");
        }
        if (producto.getNombreProducto() == null) {
            throw new IllegalArgumentException("Campo nombreProducto obligatorio");
        }
        if (producto.getStock() == null) {
            throw new IllegalArgumentException("Campo stock obligatorio");
        }

        //Validación de campos digitados
        if (producto.getProductoId().toString().length() < 4){
            throw new IllegalArgumentException("El id del producto debe tener mínimo 4 caracteres");
        }

        if (!Validador.esTextoValido(producto.getNombreProducto())) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío y solo debe contener letras");
        }

        if (!Validador.esNumeroPositivo(producto.getStock())){
            throw new IllegalArgumentException("El stock no puede ser menor o igual a 0.");
        }

        //Validar que el ID digitado ya no exista
        Producto newProducto=buscarProducto(producto.getProductoId());
        if (newProducto !=null){
            throw new IllegalArgumentException("El producto con id :" + producto.getProductoId() + " ya existe");
        }

        productos.add(producto);
    }

    //Listar Productos
    public List<Producto>listadoProductos() {
        return productos;
    }

    //Buscar producto por ID
    public Producto buscarProducto(Long id) {
        if (id != null){
            for (Producto producto : productos) {
                if (producto.getProductoId().equals(id)) {
                    return producto;
                }
            }
        }
        return null;
    }

    //Eliminar producto
    public Producto eliminarProducto(Long id) {
        Producto producto = buscarProducto(id);
        if (producto == null) {
            throw new NoSuchElementException("No existe el producto con el id :" + id);
        }
        productos.remove(producto);
        return producto;
    }


}
