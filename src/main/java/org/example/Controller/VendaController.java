package org.example.Controller;

import org.example.Model.Service.VendaService;

public class VendaController {

    private final VendaService vendaService;

    public VendaController() {
        this.vendaService = new VendaService();
    }

    public String realizarVenda(String codProd,
                                String cpfFuncionario,
                                int quantidade,
                                String cpfComprador) throws Exception {
        return vendaService.realizarVenda(codProd, cpfFuncionario, quantidade, cpfComprador);
    }
}
