package org.example.Controller;

import org.example.Model.Service.AuditoriaVendaService;

import java.util.Scanner;


public class AuditoriaVendaController {

    private final AuditoriaVendaService auditoriaService = new AuditoriaVendaService();

    public void listarAuditorias() {
        auditoriaService.listarAuditorias();
    }

    public void buscarPorComprador(Scanner scanner) {
        auditoriaService.buscarAuditoriasPorComprador(scanner);
    }

    public void buscarPorFuncionario(Scanner scanner) {
        auditoriaService.buscarAuditoriasPorFuncionario(scanner);
    }

    public void buscarPorProduto(Scanner scanner) {
        auditoriaService.buscarAuditoriasPorProduto(scanner);
    }
}
