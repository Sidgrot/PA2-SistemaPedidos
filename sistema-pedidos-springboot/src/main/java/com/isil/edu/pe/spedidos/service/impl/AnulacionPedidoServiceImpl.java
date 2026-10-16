package com.isil.edu.pe.spedidos.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isil.edu.pe.spedidos.client.DevolucionStockClient;
import com.isil.edu.pe.spedidos.client.dto.DevolucionStockRequest;
import com.isil.edu.pe.spedidos.entity.AnulacionPedido;
import com.isil.edu.pe.spedidos.entity.Pedido;
import com.isil.edu.pe.spedidos.repository.AnulacionPedidoRepository;
import com.isil.edu.pe.spedidos.repository.PedidoRepository;
import com.isil.edu.pe.spedidos.service.AnulacionPedidoService;

@Service
public class AnulacionPedidoServiceImpl implements AnulacionPedidoService {

    private final PedidoRepository pedidoRepository;
    private final AnulacionPedidoRepository anulacionRepository;
    private final DevolucionStockClient devolucionClient;

    // ¡Inyección por Constructor! (Asegura que las dependencias nunca sean nulas)
    public AnulacionPedidoServiceImpl(PedidoRepository pedidoRepository,
                                      AnulacionPedidoRepository anulacionRepository,
                                      DevolucionStockClient devolucionClient) {
        this.pedidoRepository = pedidoRepository;
        this.anulacionRepository = anulacionRepository;
        this.devolucionClient = devolucionClient;
    }

    @Transactional
    @Override
    public void anularPedido(Long pedidoId, String motivo) {
        // 1. Buscar el pedido en la base de datos local
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido inexistente"));

        // 2. Validar que no esté anulado ya (Evitar duplicados)
        if (pedido.getEstado() == Pedido.PedidoEstado.ANULADO || 
            anulacionRepository.findByPedidoId(pedidoId).isPresent()) {
            throw new IllegalStateException("Anulación duplicada");
        }

        // 3. Llamar al API de inventario para devolver el stock remoto
        DevolucionStockRequest requestDevolucion = new DevolucionStockRequest(pedido.getCantidad());
        devolucionClient.devolver(pedido.getProductoCodigo(), requestDevolucion);

        // 4. Guardar el registro de la anulación en la base de datos H2
        AnulacionPedido anulacion = new AnulacionPedido();
        anulacion.setPedidoId(pedido.getId());
        anulacion.setProductoCodigo(pedido.getProductoCodigo());
        anulacion.setCantidadDevuelta(pedido.getCantidad());
        anulacion.setFecha(LocalDateTime.now());
        anulacion.setMotivo(motivo);
        anulacionRepository.save(anulacion);

        // 5. Cambiar el estado del pedido a ANULADO
        pedido.setEstado(Pedido.PedidoEstado.ANULADO);
        pedidoRepository.save(pedido);
    }
    
    @Override
    public java.util.List<AnulacionPedido> listarAnulaciones() {
        return anulacionRepository.findAll();
    }
}
