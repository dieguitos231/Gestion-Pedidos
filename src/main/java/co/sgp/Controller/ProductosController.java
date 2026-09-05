package co.sgp.Controller;

import co.sgp.Models.Producto.Producto;
import co.sgp.Services.ProductoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/productos")
public class ProductosController {

    // Dependencia para llamar todos los métodos de productoService
    private final ProductoService productoService;
    // Constructor para inyectar productoService
    public ProductosController(ProductoService productoService) { this.productoService = productoService;}

    //Método POST

    //Crear Producto
    @PostMapping("/crear")
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            Producto productoACrear = productoService.crearProducto(producto);
            productoService.crearProducto(productoACrear);
            return ResponseEntity.status(HttpStatus.CREATED).body(producto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //Método PUT

    //Actualizar stock de un producto
    @PutMapping("/actStock/{productoID}")
    public ResponseEntity<?> actualizarStock(@PathVariable Long productoID, @RequestBody Integer nuevoStock) {
        try{
            Producto producto=productoService.actualizarStock(productoID, nuevoStock);
            return ResponseEntity.ok().body(producto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    //Métodos GET

    //Listado de productos / Buscar por ID de producto
    @GetMapping
    public ResponseEntity<?> listaProductos(@RequestParam(required = false) Long productoId) {
        if (productoId != null) {
            try {
                Producto producto = productoService.buscarProducto(productoId);
                return ResponseEntity.ok().body(producto);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            } catch (NoSuchElementException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.ok(productoService.listadoProductos());
    }

    // Buscar productos por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> buscarProductoPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok().body(productoService.busquedaPorNombre(nombre));
    }

    //Buscar productos con un stock menor al valorDigitado
    @GetMapping("/stock/{valor}")
    public ResponseEntity<?> productosConStockMenorAValor(@PathVariable Integer valor) {
        try {
            return ResponseEntity.ok().body(productoService.buscarProductosStockMenorAValor(valor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //Lista de productos con stock agotado
    @GetMapping("/stock/agotado")
    public ResponseEntity<?> listaProductosAgotados() {
        return ResponseEntity.ok().body(productoService.listaProductosAgotados());
    }

    //Eliminar producto
    @DeleteMapping("/eliminar/{productoId}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long productoId) {
        try {
            productoService.eliminarProducto(productoId);
            return ResponseEntity.ok().body("Se ha eliminado el producto con ID: " + productoId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
