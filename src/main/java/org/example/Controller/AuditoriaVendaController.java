package org.example.Controller;


import org.example.Model.Entity.AuditoriaVendaMODEL;
import org.example.Model.Service.AuditoriaVendaService;

import java.util.List;

public class AuditoriaVendaController {

    private final AuditoriaVendaService auditoriaService;

    public AuditoriaVendaController() {
        this.auditoriaService = new AuditoriaVendaService();
    }

    public List<AuditoriaVendaMODEL> listarAuditorias() {
        return auditoriaService.listarAuditorias();
    }

    public List<AuditoriaVendaMODEL> buscarPorComprador(String cpf) throws Exception {
        return auditoriaService.buscarAuditoriasPorComprador(cpf);
    }


    public List<AuditoriaVendaMODEL> buscarPorFuncionario(String cpf) throws Exception {
        return auditoriaService.buscarAuditoriasPorFuncionario(cpf);
    }

    public List<AuditoriaVendaMODEL> buscarPorProduto(Long idProduto) throws Exception {
        return auditoriaService.buscarAuditoriasPorProduto(idProduto);

    }
}
