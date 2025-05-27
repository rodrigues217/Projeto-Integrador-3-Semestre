package org.example.Controller;

import org.example.Model.Service.VendaService;

public class VendaController {
    private VendaService vendaService = new VendaService();

    public String processarVenda(Long produtoId, Long funcionarioId, int quantidade, Long compradorId) {
        return vendaService.realizarVenda(produtoId, funcionarioId, quantidade, compradorId);
    }
}
