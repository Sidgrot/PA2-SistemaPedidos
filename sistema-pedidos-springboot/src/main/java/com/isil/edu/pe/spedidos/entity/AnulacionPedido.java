package com.isil.edu.pe.spedidos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "anulacion_pedido")
public class AnulacionPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long pedidoId;

    private String productoCodigo;
    private Integer cantidadDevuelta;
    private LocalDateTime fecha;
    private String motivo;

    // --- Constructor vacío ---
    public AnulacionPedido() {
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public String getProductoCodigo() { return productoCodigo; }
    public void setProductoCodigo(String productoCodigo) { this.productoCodigo = productoCodigo; }

    public Integer getCantidadDevuelta() { return cantidadDevuelta; }
    public void setCantidadDevuelta(Integer cantidadDevuelta) { this.cantidadDevuelta = cantidadDevuelta; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
