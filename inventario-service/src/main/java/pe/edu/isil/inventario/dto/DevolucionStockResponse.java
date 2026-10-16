package pe.edu.isil.inventario.dto;

public class DevolucionStockResponse {
    
    private String codigo;
    private Integer cantidadDevuelta;
    private Integer stockRestante;
    
    public DevolucionStockResponse(String codigo, Integer cantidadDevuelta, Integer stockRestante) {
        this.codigo = codigo; 
        this.cantidadDevuelta = cantidadDevuelta; 
        this.stockRestante = stockRestante;
    }

    // --- Getters y Setters ---
    
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
