package co.sgp.Services;

import co.sgp.Models.Pedido.Estado;
import co.sgp.Models.Pedido.Pedido;
import co.sgp.Models.Pedido.Prioridad;
import co.sgp.Models.Producto.Producto;
import co.sgp.Utils.Validador;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PedidoService {
    private final List<Pedido> pedidos = new ArrayList<>();

    // Dependencia para llamar todos los métodos de productoService
    private final ProductoService productoService;

    // Constructor para inyectar productoService
    public PedidoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    public void crearPedido(Pedido pedido) {
        //Validaciones de campos necesarios
        if (pedido.getNIT() == null) {
            throw new IllegalArgumentException("Campo NIT obligatorio");
        }
        if (pedido.getNombreCliente() == null) {
            throw new IllegalArgumentException("Campo nombreCliente obligatorio");
        }
        if (pedido.getPrioridad() == null) {
            throw new IllegalArgumentException("Campo prioridad obligatorio");
        }
        if (pedido.getCantidad() == null) {
            throw new IllegalArgumentException("Campo cantidad obligatorio");
        }
        if (pedido.getProductoId() == null) {
            throw new IllegalArgumentException("Campo productoId obligatorio");
        }

        //Validación de que cantidad del pedido sea mayor a 0
        if (!Validador.esNumeroPositivo(pedido.getCantidad())) {
            throw new IllegalArgumentException("La cantidad solicitada del producto debe ser mayor a 0 .");
        }

        //Validación de que el NIT del cliente sea digitado
        if (!Validador.esNitvalido(pedido.getNIT())) {
            throw new IllegalArgumentException("El numero del NIT debe contener mas de 6 caracteres.");
        }

        //Validación del que el nombre del cliente sea digitado
        if (pedido.getNombreCliente().isBlank() || !Validador.esTextoValido(pedido.getNombreCliente())) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío y solo debe contener letras.");
        }


        //Validación de que la prioridad exista
        if (!Validador.existePrioridad(pedido.getPrioridad())) {
            throw new IllegalArgumentException("No existe la prioridad : " + pedido.getPrioridad() + " Prioridades Permitidas :" + Arrays.toString(Prioridad.values()));
        }


        Producto productoStock = productoService.buscarProducto(pedido.getProductoId());
        if (productoStock == null) {
            throw new NoSuchElementException("No existe el producto con ID : " + pedido.getProductoId());
        }

        //Validación de que el stock sea suficiente para el pedido
        if (productoStock.getStock() < pedido.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + productoStock.getStock() + ",solicitado: " + pedido.getCantidad());
        }
        pedido.setId(Pedido.generarSiguienteId());
        pedidos.add(pedido);
    }

    public List<Pedido> listaPedidos() {
        return pedidos;
    }

    public Pedido buscarPedido(Integer id) {
        if (id != null) {
            for (Pedido pedido : pedidos) {
                if (id.equals(pedido.getId())) {
                    return pedido;
                }
            }
        }
        return null;
    }

    public List<Pedido> pedidosPorEstado(String estado) {
        if (!Validador.esEstadoValido(estado)) {
            throw new NoSuchElementException("No se encontró el estado : " + estado + "\nEstados permitidos : " + Arrays.toString(Estado.values()));
        }
        if (pedidos.isEmpty()) {
            throw new NoSuchElementException("No se encontró ningún pedido registrado.");
        }
        if (!Validador.esTextoValido(estado)) {
            throw new IllegalArgumentException("El estado digitado no puede estar vacío y solo debe contener letras.");
        }

        Estado estadoEnum = Estado.valueOf(estado.trim().toUpperCase());
        List<Pedido> pedidosFiltradosPorEstado = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (pedido.getEstado().equals(estadoEnum)) {
                pedidosFiltradosPorEstado.add(pedido);
            }
        }

        if (pedidosFiltradosPorEstado.isEmpty()) {
            throw new NoSuchElementException("No se encontró ningún pedido por estado : " + estadoEnum.name());
        }

        return pedidosFiltradosPorEstado;
    }
    public List<Pedido> pedidosPorPrioridad(String prioridad) {
        if (!Validador.esPrioridadValida(prioridad)) {
            throw new NoSuchElementException("No se encontró la prioridad :" + prioridad + "\nPrioridades permitidas : " + Arrays.toString(Prioridad.values()));
        }
        if (pedidos.isEmpty()) {
            throw new NoSuchElementException("No hay pedidos registrados.");
        }
        if (!Validador.esTextoValido(prioridad)) {
            throw new IllegalArgumentException("El estado digitado no puede estar vacío y solo debe contener letras.");
        }
        Prioridad prioridadEnum = Prioridad.valueOf(prioridad.trim().toUpperCase());
        List<Pedido> pedidosFiltradosPorPrioridad = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (pedido.getPrioridad().equals(prioridadEnum)){
                pedidosFiltradosPorPrioridad.add(pedido);
            }
        }
        if (pedidosFiltradosPorPrioridad.isEmpty()) {
            throw new NoSuchElementException("No se encontró ningún pedido por prioridad : " + prioridadEnum.name());
        }
        return pedidosFiltradosPorPrioridad;
    }

    public List<Pedido> pedidosEnRiesgo() {
        if (pedidos.isEmpty()) {
            throw new NoSuchElementException("No se encontró ningún pedido registrado.");
        }
        List<Pedido> pedidosEnRiesgo = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (Estado.PENDIENTE.equals(pedido.getEstado())) {
                Producto productoStock = productoService.buscarProducto(pedido.getProductoId());
                if (productoStock == null || productoStock.getStock() < pedido.getCantidad()) {
                    pedidosEnRiesgo.add(pedido);
                }
            }
        }

        return pedidosEnRiesgo;
    }
        // Método auxiliar para darle una valor a cada prioridad
    private int obtenerValorPrioridad(Prioridad prioridad) {
        if (prioridad == null) {
            return 0;
        }
        return switch (prioridad) {
            case URGENTE -> 4;
            case ALTA -> 3;
            case MEDIA -> 2;
            case BAJA -> 1;
        };
    }
    public Pedido pedidoSiguiente(){
        if (pedidos.isEmpty()) {
            throw new NoSuchElementException("No hay  pedidos registrados.");
        }
        Pedido siguiente=null;
        for (Pedido pedido : pedidos) {

            // Solo evaluamos pedidos que estén Pendientes
            if (Estado.PENDIENTE.equals(pedido.getEstado())) {

                if (siguiente==null){
                    //Tomamos el primer pedido como referencia
                    siguiente=pedido;
                }else {
                    //Se obtine la prioridad del pedido en el bucle con el asignado a siguiente
                    int prioridadPedidoActual=obtenerValorPrioridad(pedido.getPrioridad());
                    int prioridadPedidoSiguiente=obtenerValorPrioridad(siguiente.getPrioridad());

                    //Si el pedido actual es mayor al siguiente, pasa a ser el siguiente
                    if (prioridadPedidoActual > prioridadPedidoSiguiente) {
                        siguiente=pedido;
                    }
                    //Si ambos tienen la misma prioridad, se toma el primer pedido creado con su ID pedido
                    else if (prioridadPedidoActual == prioridadPedidoSiguiente) {
                        if (pedido.getId() < siguiente.getId()) {
                            siguiente=pedido;
                        }
                    }
                }
            }

        }
        //Si depuse de recorrer toda la lista no hay pedidos pendientes
        if (siguiente==null){
            throw new NoSuchElementException("No hay pedidos pendientes por atender.");
        }
        return siguiente;
    }


    public Pedido confirmarPedido(Integer id) {
        if (!Validador.esNumeroPositivo(id)) {
            throw new IllegalArgumentException("El id debe ser mayor a 0 .");
        }

        Pedido pedido = buscarPedido(id);
        if (pedido != null) {
            if (!Estado.PENDIENTE.equals(pedido.getEstado())) {
                throw new IllegalArgumentException("No se puede confirmar pedidos que no se encuentren en estado 'PENDIENTE'.");
            }
            Producto producto = productoService.buscarProducto(pedido.getProductoId());
            if (producto == null) {
                throw new NoSuchElementException("No existe el producto con el id: " + pedido.getProductoId());
            }
            if (producto.getStock() < pedido.getCantidad()) {
                throw new IllegalArgumentException("stock insuficiente para confirmar el pedido. Disponible : " + producto.getStock() + ",solicitado :" + pedido.getCantidad());
            }
            producto.setStock(producto.getStock() - pedido.getCantidad());
            pedido.setEstado(Estado.CONFIRMADO);
            return pedido;
        }
        throw new NoSuchElementException("No se encontró el pedido con ID : " + id);
    }

    public List<String> resumenPedidos() {
        if (pedidos.isEmpty()) {
            throw new NoSuchElementException("No hay pedidos registrados.");
        }
        List<Pedido> pedidosPendientes = new ArrayList<>();
        List<Pedido> pedidosConfirmados = new ArrayList<>();
        List<Pedido> pedidosDespachados = new ArrayList<>();
        List<Pedido> pedidosCancelados = new ArrayList<>();
        List<Pedido> pedidosUrgentes = new ArrayList<>();

        //Total de pedidos
        for (Pedido pedido : pedidos) {
            switch (pedido.getEstado()) {
                case Estado.PENDIENTE -> pedidosPendientes.add(pedido);
                case Estado.CONFIRMADO -> pedidosConfirmados.add(pedido);
                case Estado.CANCELADO -> pedidosCancelados.add(pedido);
                case Estado.DESPACHADO -> pedidosDespachados.add(pedido);
            }
        }
        for (Pedido pedido : pedidos) {
            if (Prioridad.URGENTE.equals(pedido.getPrioridad())) {
                pedidosUrgentes.add(pedido);
            }
        }


        List<String> resumenPedidos = new ArrayList<>();
        resumenPedidos.add("Total Pedidos : " + pedidos.size());
        resumenPedidos.add("Total Pedidos Pendientes : " + pedidosPendientes.size());
        resumenPedidos.add("Total Pedidos Confirmados : " + pedidosConfirmados.size());
        resumenPedidos.add("Total Pedidos Despachados : " + pedidosDespachados.size());
        resumenPedidos.add("Total Pedidos Cancelados : " + pedidosCancelados.size());
        resumenPedidos.add("Total Pedidos Urgentes : " + pedidosUrgentes.size());

        return resumenPedidos;
    }


    public Pedido despacharPedido(Integer id) {
        if (!Validador.esNumeroPositivo(id)) {
            throw new IllegalArgumentException("El id debe ser mayor a 0 .");
        }
        Pedido pedido = buscarPedido(id);
        if (pedido != null) {
            if (!Estado.CONFIRMADO.equals(pedido.getEstado())) {
                throw new IllegalArgumentException("Solo se pueden despachar pedidos en estado 'CONFIRMADO'.");
            }
            pedido.setEstado(Estado.DESPACHADO);
            return pedido;
        }
        throw new NoSuchElementException("No se encontró el pedido con ID : " + id);
    }

    public Pedido cancelarPedido(Integer id) {
        if (!Validador.esNumeroPositivo(id)) {
            throw new IllegalArgumentException("El id debe ser mayor a 0 .");
        }
        Pedido pedido = buscarPedido(id);
        if (pedido != null) {
            if (Estado.CANCELADO.equals(pedido.getEstado()) || Estado.DESPACHADO.equals(pedido.getEstado())) {
                throw new IllegalArgumentException("El pedido con ID : " + id + " . Se encuentra en estado :" + pedido.getEstado() + " .Solo se pueden cancelar pedidos en estado PENDIENTE O CONFIRMADO");
            }
            if (Estado.PENDIENTE.equals(pedido.getEstado())) {
                pedido.setEstado(Estado.CANCELADO);
                return pedido;
            }
            Producto producto = productoService.buscarProducto(pedido.getProductoId());
            if (producto == null) {
                throw new NoSuchElementException("No se encontro el producto con ID : " + id);
            }
            producto.setStock(producto.getStock() + pedido.getCantidad());
            pedido.setEstado(Estado.CANCELADO);
            return pedido;
        }
        throw new NoSuchElementException("No se encontró el pedido con ID : " + id);
    }

}
