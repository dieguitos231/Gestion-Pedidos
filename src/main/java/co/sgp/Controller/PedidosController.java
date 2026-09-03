package co.sgp.Controller;

import co.sgp.Services.PedidoService;

import co.sgp.Models.Pedido.Pedido;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/pedidos")
public class PedidosController {

    // Dependencia para llamar todos los métodos de pedidoService
    private final PedidoService pedidoService;

    // Constructor para inyectar pedidoService
    public PedidosController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    //Listado de productos / Buscar por ID de producto
    @GetMapping
    public ResponseEntity<?> listarPedidos(@RequestParam(required = false) Integer id) {
        if (id != null) {
            Pedido pedido = pedidoService.buscarPedido(id);
            if (pedido != null) {
                return ResponseEntity.ok().body(pedido);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe el pedido con id: " + id);
        }
        return ResponseEntity.ok().body(pedidoService.listaPedidos());
    }

    //Método POST

    // Crear Pedido
    @PostMapping("/crear")
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        try {
            pedidoService.crearPedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Métodos PUT

    //Método para Confirmar pedidos en estado PENDIENTE
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarPedido(@PathVariable Integer id) {
        try {
            Pedido pedido = pedidoService.confirmarPedido(id);
            return ResponseEntity.ok(pedido);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //Método para Cancelar pedidos
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Integer id) {
        try {
            Pedido pedido = pedidoService.cancelarPedido(id);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    //Método para Despachar pedidos en estado Confirmado
    @PutMapping("/{id}/despachar")
    public ResponseEntity<?> despacharPedido(@PathVariable Integer id) {
        try {
            Pedido pedido = pedidoService.despacharPedido(id);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Métodos GET

    //Búsqueda de pedidos por estados
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> listaEstado(@PathVariable String estado) {
        try {
            List<Pedido> pedidos = pedidoService.pedidosPorEstado(estado);
            return ResponseEntity.ok(pedidos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Búsqueda de pedidos por prioridades
    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<?> listaPrioridad(@PathVariable String prioridad) {
        try {
            List<Pedido> pedidos = pedidoService.pedidosPorPrioridad(prioridad);
            return ResponseEntity.ok(pedidos);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

     //Resumen de pedidos
    @GetMapping("/resumen")
    public ResponseEntity<?> listaEstadoResumen() {
        try {
            List<String> resumen =pedidoService.resumenPedidos();
            return ResponseEntity.ok(resumen);
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Mostrar pedidos en riesgo
    @GetMapping("/riesgo")
    public ResponseEntity<?> obtenerPedidosEnRiesgo() {
        try {
            List<Pedido> pedidosEnRiego=pedidoService.pedidosEnRiesgo();
            return ResponseEntity.ok(pedidosEnRiego);
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Conocer qué pedido será atendido primero.
    @GetMapping("/siguiente")
    public ResponseEntity<?> obtenerSiguientePedido() {
        try {
            Pedido pedidoSiguiente=pedidoService.pedidoSiguiente();
            return ResponseEntity.ok(pedidoSiguiente);
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
