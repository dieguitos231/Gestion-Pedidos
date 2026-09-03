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

    private final ProductoService productoService;

    public ProductosController(ProductoService productoService){
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<?> listaProductos(){
        return ResponseEntity.ok(productoService.listadoProductos());
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto){
        try {
            productoService.crearProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(producto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<?> eliminarProducto(@PathVariable("productoId") Long productoId){
        try{
            Producto productoEliminado=productoService.eliminarProducto(productoId);
            return ResponseEntity.ok().body(productoEliminado);
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe el producto con el id :" + productoId);
        }
    }
}
