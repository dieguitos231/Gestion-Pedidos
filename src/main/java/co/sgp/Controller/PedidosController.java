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


    //Método POST

    // Crear nuevo Pedido
    @PostMapping("/crear")
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        try {
            Pedido pedidoACrear=pedidoService.crearPedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoACrear);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    //Métodos PUT

    //Confirmar pedidos en estado pendiente
    @PutMapping("/{pedidoId}/confirmar")
    public ResponseEntity<?> confirmarPedido(@PathVariable Long pedidoId) {
        try {
            Pedido pedido = pedidoService.confirmarPedido(pedidoId);
            return ResponseEntity.ok(pedido);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //Cancelar pedidos
    @PutMapping("/{pedidoId}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long pedidoId) {
        try {
            Pedido pedido = pedidoService.cancelarPedido(pedidoId);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Despachar pedidos en estado confirmado
    @PutMapping("/{pedidoId}/despachar")
    public ResponseEntity<?> despacharPedido(@PathVariable Long pedidoId) {
        try {
            Pedido pedido = pedidoService.despacharPedido(pedidoId);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Métodos GET

    //Listado de pedidos / Buscar por ID de pedido
    @GetMapping
    public ResponseEntity<?> listarPedidos(@RequestParam(required = false) Long pedidoId) {
        if (pedidoId != null) {
            try {
                Pedido pedido = pedidoService.buscarPedido(pedidoId);
                return ResponseEntity.ok().body(pedido);
            } catch (NoSuchElementException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.ok().body(pedidoService.listaPedidos());
    }

    //Buscar pedidos por estado
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

    //Buscar pedidos por prioridad
    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<?> listaPrioridad(@PathVariable String prioridad) {
        try {
            List<Pedido> pedidos = pedidoService.pedidosPorPrioridad(prioridad);
            return ResponseEntity.ok(pedidos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Resumen de pedidos
    @GetMapping("/resumen")
    public ResponseEntity<?> listaEstadoResumen() {
        try {
            List<String> resumen = pedidoService.resumenPedidos();
            return ResponseEntity.ok(resumen);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Pedidos en riesgo
    @GetMapping("/riesgo")
    public ResponseEntity<?> obtenerPedidosEnRiesgo() {
        List<Pedido> pedidosEnRiego = pedidoService.pedidosEnRiesgo();
        return ResponseEntity.ok(pedidosEnRiego);
    }

   //Pedido que será atendido primero.
    @GetMapping("/siguiente")
    public ResponseEntity<?> obtenerSiguientePedido() {
        try {
            Pedido pedidoSiguiente = pedidoService.pedidoSiguiente();
            return ResponseEntity.ok(pedidoSiguiente);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
