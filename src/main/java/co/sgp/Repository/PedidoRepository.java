package co.sgp.Repository;

import co.sgp.Models.Pedido.Estado;
import co.sgp.Models.Pedido.Pedido;
import co.sgp.Models.Pedido.Prioridad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByPrioridad(Prioridad prioridad);
    List<Pedido> findByEstado(Estado estado);
}
