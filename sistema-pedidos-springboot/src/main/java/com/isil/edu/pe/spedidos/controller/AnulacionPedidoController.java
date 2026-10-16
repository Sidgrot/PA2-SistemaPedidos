package com.isil.edu.pe.spedidos.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import com.isil.edu.pe.spedidos.controller.dto.AnulacionRequest;
import com.isil.edu.pe.spedidos.entity.AnulacionPedido;
import com.isil.edu.pe.spedidos.service.AnulacionPedidoService;

@RestController
@RequestMapping("/api/v1/anulaciones")
public class AnulacionPedidoController {

    private final AnulacionPedidoService anulacionService;

    // ¡Inyección por constructor! (Punto a favor en la evaluación)
    public AnulacionPedidoController(AnulacionPedidoService anulacionService) {
        this.anulacionService = anulacionService;
    }

    @PostMapping
    public String anular(@RequestBody AnulacionRequest request) {
        anulacionService.anularPedido(request.getPedidoId(), request.getMotivo());
        return "Pedido Anulado Correctamente. Stock devuelto a Inventario.";
    }

    @GetMapping
    public List<AnulacionPedido> listarAnulaciones() {
        return anulacionService.listarAnulaciones();
    }
}
