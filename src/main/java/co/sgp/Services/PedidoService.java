package co.sgp.Services;

import co.sgp.Models.Pedido.Estado;
import co.sgp.Models.Pedido.Pedido;
import co.sgp.Models.Pedido.Prioridad;
import co.sgp.Models.Producto.Producto;
import co.sgp.Repository.PedidoRepository;
import co.sgp.Utils.Validador;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    // Dependencia para llamar todos los métodos de productoService
    private final ProductoService productoService;

    // Constructor para inyectar productoService y pedidoRepository
    public PedidoService(ProductoService productoService, PedidoRepository pedidoRepository) {
        this.productoService = productoService;
        this.pedidoRepository = pedidoRepository;
    }

    //Crear Pedido
    public Pedido crearPedido(Pedido pedido) {
        //Validaciones de campos
        if (pedido.getNit() == null) {
            throw new IllegalArgumentException("Campo Nit obligatorio");
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

        //Validación de campos digitados
        if (!Validador.esNumeroPositivo(pedido.getCantidad())) {
            throw new IllegalArgumentException("La cantidad solicitada del producto debe ser mayor a 0 .");
        }

        if (!Validador.esNitvalido(pedido.getNit())) {
            throw new IllegalArgumentException("El numero del NIT debe contener mas de 6 caracteres.");
        }

        if (pedido.getNombreCliente().isBlank() || !Validador.esTextoValido(pedido.getNombreCliente())) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío y solo debe contener letras.");
        }

        Producto productoStock = productoService.buscarProducto(pedido.getProductoId());
        if (productoStock == null) {
            throw new NoSuchElementException("No existe el producto con ID : " + pedido.getProductoId());
        }

        //Validación de que el stock sea suficiente para el pedido
        if (productoStock.getStock() < pedido.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + productoStock.getStock() + ",solicitado: " + pedido.getCantidad());
        }

        pedidoRepository.save(pedido);
        return pedido;
    }

    //Confirmar pedidos en estado pendiente
    public Pedido confirmarPedido(Long pedidoId) {
        Pedido pedido = buscarPedido(pedidoId);
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
            return pedidoRepository.save(pedido);
        }
        throw new NoSuchElementException("No se encontró el pedido con ID : " + pedidoId);
    }

    //Cancelar pedidos en estado pendiente o confirmado
    public Pedido cancelarPedido(Long pedidoId) {
        Pedido pedido = buscarPedido(pedidoId);
        if (pedido != null) {
            if (Estado.CANCELADO.equals(pedido.getEstado()) || Estado.DESPACHADO.equals(pedido.getEstado())) {
                throw new IllegalArgumentException("El pedido con ID : " + pedidoId + " . Se encuentra en estado :" + pedido.getEstado() + " .Solo se pueden cancelar pedidos en estado PENDIENTE O CONFIRMADO");
            }
            if (Estado.PENDIENTE.equals(pedido.getEstado())) {
                pedido.setEstado(Estado.CANCELADO);
                pedidoRepository.save(pedido);
                return pedido;
            }
            Producto producto = productoService.buscarProducto(pedido.getProductoId());
            if (producto == null) {
                throw new NoSuchElementException("No se encontró el producto con ID : " + pedidoId);
            }
            producto.setStock(producto.getStock() + pedido.getCantidad());
            pedido.setEstado(Estado.CANCELADO);
            pedidoRepository.save(pedido);
            return pedido;
        }
        throw new NoSuchElementException("No se encontró el pedido con ID : " + pedidoId);
    }

    //Despachar pedidos en estado confirmado
    public Pedido despacharPedido(Long pedidoId) {
        Pedido pedido = buscarPedido(pedidoId);
        if (pedido != null) {
            if (!Estado.CONFIRMADO.equals(pedido.getEstado())) {
                throw new IllegalArgumentException("Solo se pueden despachar pedidos en estado 'CONFIRMADO'.");
            }
            pedido.setEstado(Estado.DESPACHADO);
            return pedidoRepository.save(pedido);
        }
        throw new NoSuchElementException("No se encontró el pedido con ID : " + pedidoId);
    }



    //Lista de pedidos
    public List<Pedido> listaPedidos() {
        return pedidoRepository.findAll();
    }

    //Buscar pedidos por ID
    public Pedido buscarPedido(Long pedidoId) {
        if (!pedidoRepository.existsById(pedidoId)) {
            throw new NoSuchElementException("No existe el pedido con ID : " + pedidoId);
        }
        return pedidoRepository.findById(pedidoId).orElse(null);
    }

    //Buscar pedidos por estado
    public List<Pedido> pedidosPorEstado(String estado) {
        if (!Validador.esTextoValido(estado)) {
            throw new IllegalArgumentException("El estado digitado no puede estar vacío y solo debe contener letras.");
        }

        if (!Validador.esEstadoValido(estado)) {
            throw new NoSuchElementException("No se encontró el estado : " + estado + "\nEstados permitidos : " + Arrays.toString(Estado.values()));
        }

        Estado estadoEnum = Estado.valueOf(estado.trim().toUpperCase());

        List<Pedido> pedidosFiltradosPorEstado = pedidoRepository.findByEstado(estadoEnum);
        if (pedidosFiltradosPorEstado.isEmpty()) {
            throw new NoSuchElementException("No se encontró ningún pedido por estado : " + estadoEnum.name());
        }

        return pedidosFiltradosPorEstado;
    }

    //Buscar pedidos por prioridad
    public List<Pedido> pedidosPorPrioridad(String prioridad) {
        if (!Validador.esTextoValido(prioridad)) {
            throw new IllegalArgumentException("El estado digitado no puede estar vacío y solo debe contener letras.");
        }
        if (!Validador.esPrioridadValida(prioridad)) {
            throw new NoSuchElementException("No se encontró la prioridad :" + prioridad + "\nPrioridades permitidas : " + Arrays.toString(Prioridad.values()));
        }

        Prioridad prioridadEnum = Prioridad.valueOf(prioridad.trim().toUpperCase());
        List<Pedido> pedidosFiltradosPorPrioridad = pedidoRepository.findByPrioridad(prioridadEnum);

        if (pedidosFiltradosPorPrioridad.isEmpty()) {
            throw new NoSuchElementException("No se encontró ningún pedido por prioridad : " + prioridadEnum.name());
        }
        return pedidosFiltradosPorPrioridad;
    }

    //Resumen de pedidos
    public List<String> resumenPedidos() {
        List<Pedido> totalPedidos = pedidoRepository.findAll();
        List<Pedido> pedidosPendientes = pedidoRepository.findByEstado(Estado.PENDIENTE);
        List<Pedido> pedidosConfirmados = pedidoRepository.findByEstado(Estado.CONFIRMADO);
        List<Pedido> pedidosDespachados = pedidoRepository.findByEstado(Estado.DESPACHADO);
        List<Pedido> pedidosCancelados = pedidoRepository.findByEstado(Estado.CANCELADO);
        List<Pedido> pedidosUrgentes = pedidoRepository.findByPrioridad(Prioridad.URGENTE);


        List<String> resumenPedidos = new ArrayList<>();
        resumenPedidos.add("Total Pedidos : " + totalPedidos.size());
        resumenPedidos.add("Total Pedidos Pendientes : " + pedidosPendientes.size());
        resumenPedidos.add("Total Pedidos Confirmados : " + pedidosConfirmados.size());
        resumenPedidos.add("Total Pedidos Despachados : " + pedidosDespachados.size());
        resumenPedidos.add("Total Pedidos Cancelados : " + pedidosCancelados.size());
        resumenPedidos.add("Total Pedidos Urgentes : " + pedidosUrgentes.size());

        return resumenPedidos;
    }

    //Pedidos en riesgo
    public List<Pedido> pedidosEnRiesgo() {
        List<Pedido> pedidosEnRiesgo = new ArrayList<>();
        List<Pedido> pedidosPendientes = pedidoRepository.findByEstado(Estado.PENDIENTE);

        // Mapa para rastrear el stock disponible virtual por productoId
        Map<Long, Integer> stockVirtualMap = new HashMap<>();

        for (Pedido pedido : pedidosPendientes) {
            Long productoId = pedido.getProductoId();

            // Cargar stock inicial del producto en el mapa si aún no está cargado
            if (!stockVirtualMap.containsKey(productoId)) {
                Producto producto = productoService.buscarProducto(productoId);
                int stockDisponible = (producto != null) ? producto.getStock() : 0;
                stockVirtualMap.put(productoId, stockDisponible);
            }

            int stockActualVirtual = stockVirtualMap.get(productoId);

            // Verificar si alcanza el stock virtual disponible para este pedido
            if (stockActualVirtual < pedido.getCantidad()) {
                pedidosEnRiesgo.add(pedido);
            } else {
                // Descontar virtualmente el stock reservado para los siguientes pedidos
                stockVirtualMap.put(productoId, stockActualVirtual - pedido.getCantidad());
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

    //Pedido que será atendido primero.
    public Pedido pedidoSiguiente() {
        List<Pedido> pedidosPendientes = pedidoRepository.findByEstado(Estado.PENDIENTE);
        Pedido siguiente = null;
        // Solo evaluamos pedidos que estén Pendientes
        for (Pedido pedido : pedidosPendientes) {
            if (siguiente == null) {
                //Tomamos el primer pedido como referencia
                siguiente = pedido;
            } else {
                //Se obtiene la prioridad del pedido en el bucle con el asignado a siguiente
                int prioridadPedidoActual = obtenerValorPrioridad(pedido.getPrioridad());
                int prioridadPedidoSiguiente = obtenerValorPrioridad(siguiente.getPrioridad());

                //Si el pedido actual es mayor al siguiente, pasa a ser el siguiente
                if (prioridadPedidoActual > prioridadPedidoSiguiente) {
                    siguiente = pedido;
                }
                //Si ambos tienen la misma prioridad, se toma el primer pedido creado con su ID pedido
                else if (prioridadPedidoActual == prioridadPedidoSiguiente) {
                    if (pedido.getPedidoId() < siguiente.getPedidoId()) {
                        siguiente = pedido;
                    }
                }
            }
        }
        //Si después de recorrer toda la lista no hay pedidos pendientes
        if (siguiente == null) {
            throw new NoSuchElementException("No hay pedidos pendientes por atender.");
        }
        return siguiente;
    }


}
