package com.isil.edu.pe.spedidos.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.isil.edu.pe.spedidos.client.dto.DevolucionStockRequest;
import com.isil.edu.pe.spedidos.client.dto.DevolucionStockResponse;

@Component
public class DevolucionStockRestClient implements DevolucionStockClient {

    private final RestClient restClient;

    // Inyección por Constructor
    public DevolucionStockRestClient(RestClient inventarioHttpClient) {
        this.restClient = inventarioHttpClient;
    }

    @Override
    public DevolucionStockResponse devolver(String codigo, DevolucionStockRequest request) {
        return restClient.post()
                .uri("http://localhost:8082/inventario-service/api/inventario/productos/codigo/{codigo}/devoluciones", codigo)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new RuntimeException("Error al devolver stock: Datos inválidos");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    throw new InventarioNoDisponibleException("El servicio de inventario no está disponible");
                })
                .body(DevolucionStockResponse.class);
    }
}
