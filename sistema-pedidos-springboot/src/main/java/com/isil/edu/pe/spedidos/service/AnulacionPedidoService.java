package com.isil.edu.pe.spedidos.service;

public interface AnulacionPedidoService {
    
    void anularPedido(Long pedidoId, String motivo);

    java.util.List<com.isil.edu.pe.spedidos.entity.AnulacionPedido> listarAnulaciones();
}
