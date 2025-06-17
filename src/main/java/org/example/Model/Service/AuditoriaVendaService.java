package org.example.Model.Service;

import org.example.Model.Entity.AuditoriaVendaMODEL;
import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Repository.AuditoriaVendaRepository;
import org.example.Model.Repository.CompradorRepository;
import org.example.Model.Repository.FuncionarioRepository;
import org.example.Model.Repository.ProdutosRepository;

import java.util.List;

public class AuditoriaVendaService {

    private final AuditoriaVendaRepository auditoriaRepository = new AuditoriaVendaRepository();
    private final CompradorRepository compradorRepository = new CompradorRepository();
    private final FuncionarioRepository funcionarioRepository = new FuncionarioRepository();
    private final ProdutosRepository produtosRepository = new ProdutosRepository();

    public List<AuditoriaVendaMODEL> listarAuditorias() {
        return auditoriaRepository.listarTodos();
    }

    public List<AuditoriaVendaMODEL> buscarAuditoriasPorComprador(String CPF) throws Exception {
        CompradorMODEL comprador = compradorRepository.buscarPorCPF(CPF);

        if (comprador == null) {
            throw new Exception("Comprador não encontrado.");
        }

        List<AuditoriaVendaMODEL> auditorias = auditoriaRepository.buscarPorCompradorCPF(CPF);

        if (auditorias.isEmpty()) {
            throw new Exception("Nenhuma auditoria encontrada para esse comprador.");
        }
        return auditorias;
    }

    public List<AuditoriaVendaMODEL> buscarAuditoriasPorFuncionario(String CPF) throws Exception {
        FuncionarioMODEL funcionario = funcionarioRepository.buscarFuncionarioPorCPF(CPF);

        if (funcionario == null) {
            throw new Exception("Funcionário não encontrado com o CPF informado.");
        }

        List<AuditoriaVendaMODEL> auditorias = auditoriaRepository.buscarPorFuncionarioCPF(CPF);

        if (auditorias.isEmpty()) {
            throw new Exception("Nenhuma auditoria encontrada para esse funcionário.");
        }
        return auditorias;
    }

    public List<AuditoriaVendaMODEL> buscarAuditoriasPorProduto(Long id) throws Exception {
        ProdutosMODEL produto = produtosRepository.buscarPorId(id);

        if (produto == null) {
            throw new Exception("Produto não encontrado.");
        }

        List<AuditoriaVendaMODEL> auditorias = auditoriaRepository.buscarPorProdutoId(id);

        if (auditorias.isEmpty()) {
            throw new Exception("Nenhuma auditoria encontrada para esse produto.");
        }
        return auditorias;
    }
}


