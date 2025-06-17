package org.example.Controller;

import org.example.Model.Service.VendaService;

import java.util.Scanner;

public class VendaController {
    private VendaService vendaService = new VendaService();

    public void iniciarProcessoDeVenda(Scanner scanner) {
        vendaService.executarVendaViaMenu(scanner);
    }
}
