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
import java.util.Scanner;

public class AuditoriaVendaService {

    private final AuditoriaVendaRepository auditoriaRepository = new AuditoriaVendaRepository();
    private final CompradorRepository compradorRepository = new CompradorRepository();
    private final FuncionarioRepository funcionarioRepository = new FuncionarioRepository();
    private final ProdutosRepository produtosRepository = new ProdutosRepository();

    public void listarAuditorias() {
        List<AuditoriaVendaMODEL> auditorias = auditoriaRepository.listarTodos();

        if (auditorias.isEmpty()) {
            System.out.println("Nenhuma auditoria encontrada.");
            return;
        }

        System.out.println("--- Auditorias de Vendas ---");
        for (AuditoriaVendaMODEL a : auditorias) {
            imprimirAuditoria(a);
        }
    }

    public void buscarAuditoriasPorComprador(Scanner scanner) {
        List<CompradorMODEL> compradores = compradorRepository.listarTodos();

        if (compradores.isEmpty()) {
            System.out.println("Nenhum comprador cadastrado.");
            return;
        }

        System.out.print("Digite o CPF do comprador: ");
        String CPF = (scanner.nextLine());
        CompradorMODEL comprador = compradorRepository.buscarPorCPF(CPF);

        if (comprador == null) {
            System.out.println("Comprador não encontrado.");
            return;
        }

        List<AuditoriaVendaMODEL> auditorias = auditoriaRepository.buscarPorCompradorCPF(CPF);

        if (auditorias.isEmpty()) {
            System.out.println("Nenhuma auditoria encontrada para esse comprador.");
            return;
        }

        System.out.println("--- Auditorias de Vendas para o comprador \"" + comprador.getNome() + "\" ---");
        for (AuditoriaVendaMODEL a : auditorias) {
            imprimirAuditoria(a);
        }
    }

    public void buscarAuditoriasPorFuncionario(Scanner scanner) {
        List<FuncionarioMODEL> funcionarios = funcionarioRepository.listarTodos();

        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }

        System.out.print("Digite o CPF do funcionário: ");
        String CPF = scanner.nextLine();

        FuncionarioMODEL funcionario = funcionarioRepository.buscarFuncionarioPorCPF(CPF); // você precisa ter esse método

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado com o CPF informado.");
            return;
        }

        List<AuditoriaVendaMODEL> auditorias = auditoriaRepository.buscarPorFuncionarioCPF(CPF);

        if (auditorias.isEmpty()) {
            System.out.println("Nenhuma auditoria encontrada para esse funcionário.");
            return;
        }

        System.out.println("--- Auditorias de Vendas realizadas pelo funcionário \"" + funcionario.getNome() + "\" ---");
        for (AuditoriaVendaMODEL a : auditorias) {
            imprimirAuditoria(a);
        }
    }

    public void buscarAuditoriasPorProduto(Scanner scanner) {
        List<ProdutosMODEL> produtos = produtosRepository.listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        System.out.print("Digite o ID do produto: ");
        Long id = Long.parseLong(scanner.nextLine());
        ProdutosMODEL produto = produtosRepository.buscarPorId(id);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        List<AuditoriaVendaMODEL> auditorias = auditoriaRepository.buscarPorProdutoId(id);

        if (auditorias.isEmpty()) {
            System.out.println("Nenhuma auditoria encontrada para esse produto.");
            return;
        }

        System.out.println("--- Auditorias de Vendas do produto \"" + produto.getNome() + "\" ---");
        for (AuditoriaVendaMODEL a : auditorias) {
            imprimirAuditoria(a);
        }
    }

    private void imprimirAuditoria(AuditoriaVendaMODEL a) {
        System.out.println(
                "ID: " + a.getId() +
                        " | Produto: " + (a.getProduto() != null ? a.getProduto().getNome() : "N/A") +
                        " | Quantidade: " + a.getQuantidade() +
                        " | Funcionário: " + (a.getFuncionario() != null ? a.getFuncionario().getNome() : "N/A") +
                        " | Comprador: " + (a.getComprador() != null ? a.getComprador().getNome() : "N/A") +
                        " | Data: " + (a.getDataVenda() != null ? a.getDataVenda().toString() : "N/A")
        );
    }
}
