package com.isil.edu.pe.spedidos.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isil.edu.pe.spedidos.client.DevolucionStockClient;
import com.isil.edu.pe.spedidos.client.InventarioClient;
import com.isil.edu.pe.spedidos.client.dto.DevolucionStockRequest;
import com.isil.edu.pe.spedidos.client.dto.ProductoInventarioResponse;
import com.isil.edu.pe.spedidos.client.dto.ReservaStockResponse;
import com.isil.edu.pe.spedidos.entity.Pedido;
import com.isil.edu.pe.spedidos.exception.PedidoException;
import com.isil.edu.pe.spedidos.repository.PedidoRepository;
import com.isil.edu.pe.spedidos.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final InventarioClient inventarioClient;
    private final DevolucionStockClient devolucionClient;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, 
                             InventarioClient inventarioClient,
                             DevolucionStockClient devolucionClient) {
        this.pedidoRepository = pedidoRepository;
        this.inventarioClient = inventarioClient;
        this.devolucionClient = devolucionClient;
    }

    @Transactional
    @Override
    public Pedido registrarPedido(String cliente, String productoCodigo, int cantidad) {
        
        // 1. Validar datos básicos
        validarDatos(cliente, productoCodigo, cantidad);

        // 2. Reserva remota en Inventario (Con el objeto ReservaStockRequest)
        try {
        	ReservaStockResponse reserva = inventarioClient.reservarStock(productoCodigo, cantidad);
        	validarReserva(reserva);
        } catch (Exception e) {
            throw new PedidoException(e.getMessage(), e);
        }

        // 3. Crear el objeto Pedido localmente solo con los datos recibidos
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setProductoCodigo(productoCodigo);
        pedido.setCantidad(cantidad);
        pedido.setFecha(LocalDateTime.now());
        pedido.setProductoNombre("Producto " + productoCodigo); // Nombre genérico para evitar errores
        pedido.setPrecioUnitario(java.math.BigDecimal.valueOf(1500.0));
        pedido.setTotal(java.math.BigDecimal.valueOf(1500.0 * cantidad));
        pedido.setEstado(Pedido.PedidoEstado.REGISTRADO);

        // 4. RETO AVANZADO: Guardado local con compensación
        try {
            if ("FALLO_LOCAL".equalsIgnoreCase(pedido.getCliente())) {
                throw new RuntimeException("Error simulado al intentar guardar en base de datos H2");
            }
            return pedidoRepository.save(pedido);
        } catch (Exception e) {
            // COMPENSACIÓN: Como falló la BD local, devolvemos el stock al Inventario
            try {
                DevolucionStockRequest peticionDevolucion = new DevolucionStockRequest(pedido.getCantidad());
                devolucionClient.devolver(pedido.getProductoCodigo(), peticionDevolucion);
                System.out.println("COMPENSACIÓN EXITOSA: Stock devuelto al inventario tras fallo local.");
            } catch (Exception exCompensacion) {
                System.err.println("ALERTA CRÍTICA: No se pudo devolver el stock. " + exCompensacion.getMessage());
            }
            throw new RuntimeException("Reserva fallida localmente. Stock remoto compensado.", e);
        }
    }

    @Override
    public List<ProductoInventarioResponse> listarProductos() {
        return inventarioClient.listarProductos();
    }

    @Override
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    private void validarDatos(String cliente, String productoCodigo, int cantidad) {
        if (cliente == null || cliente.isBlank()) {
            throw new PedidoException("El cliente es obligatorio.");
        }
        if (productoCodigo == null || productoCodigo.isBlank()) {
            throw new PedidoException("El código de producto es obligatorio.");
        }
        if (cantidad <= 0) {
            throw new PedidoException("La cantidad debe ser mayor a cero.");
        }
    }

    private void validarReserva(ReservaStockResponse reserva) {
        if (reserva == null || reserva.getCodigo() == null) {
            throw new PedidoException("No se pudo confirmar la reserva en el inventario.");
        }
    }
}