package org.example.Model.Service;

import org.example.Model.Entity.AuditoriaVendaMODEL;
import org.example.Model.Repository.AuditoriaVendaRepository;

import java.util.List;

public class AuditoriaVendaService {

    private final AuditoriaVendaRepository auditoriaRepository = new AuditoriaVendaRepository();

    public List<AuditoriaVendaMODEL> listarAuditorias() {
        return auditoriaRepository.listarTodos();
    }

    public List<AuditoriaVendaMODEL> buscarAuditoriasPorComprador(String cpf) throws Exception {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new Exception("CPF do comprador não pode ser vazio.");
        }


        List<AuditoriaVendaMODEL> auditorias = auditoriaRepository.buscarPorCompradorCPF(cpf);


        if (auditorias.isEmpty()) {
            throw new Exception("Nenhuma auditoria encontrada para o comprador com CPF: " + cpf);
        }

        return auditorias;
    }

    public List<AuditoriaVendaMODEL> buscarAuditoriasPorFuncionario(String cpf) throws Exception {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new Exception("CPF do funcionário não pode ser vazio.");
        }

        List<AuditoriaVendaMODEL> auditorias = auditoriaRepository.buscarPorFuncionarioCPF(cpf);
        if (auditorias.isEmpty()) {
            throw new Exception("Nenhuma auditoria encontrada para o funcionário com CPF: " + cpf);
        }

        return auditorias;
    }

    public List<AuditoriaVendaMODEL> buscarAuditoriasPorProduto(Long idProduto) throws Exception {
        if (idProduto == null) {
            throw new Exception("ID do produto não pode ser nulo.");
        }

        List<AuditoriaVendaMODEL> auditorias = auditoriaRepository.buscarPorProdutoId(idProduto);
        if (auditorias.isEmpty()) {
            throw new Exception("Nenhuma auditoria encontrada para o produto com ID: " + idProduto);
        }

        return auditorias;
    }
}

