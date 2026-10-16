package com.isil.edu.pe.spedidos.client.dto;

public class DevolucionStockResponse {
    
    private String codigo;
    private Integer cantidadDevuelta;
    private Integer stockRestante;

    public DevolucionStockResponse() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getCantidadDevuelta() {
        return cantidadDevuelta;
    }

    public void setCantidadDevuelta(Integer cantidadDevuelta) {
        this.cantidadDevuelta = cantidadDevuelta;
    }

    public Integer getStockRestante() {
        return stockRestante;
    }

    public void setStockRestante(Integer stockRestante) {
        this.stockRestante = stockRestante;
    }
}
