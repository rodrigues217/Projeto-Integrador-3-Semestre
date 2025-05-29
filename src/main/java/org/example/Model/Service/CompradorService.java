package org.example.Model.Service;

import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Repository.CompradorRepository;

import java.util.List;
import java.util.Scanner;

public class CompradorService {
    private CompradorRepository compradorRepository = new CompradorRepository();
    private Scanner scanner = new Scanner(System.in);

    public void criarComprador() {
        System.out.print("Nome do comprador: ");
        String nome = scanner.nextLine();

        System.out.print("Telefone do comprador: ");
        String telefone = scanner.nextLine();

        System.out.print("CPF do comprador: ");
        String CPF = scanner.nextLine();

        List<CompradorMODEL> compradores = compradorRepository.listarTodos();

        boolean nomeDuplicado = false;
        boolean telefoneDuplicado = false;
        boolean cpfDuplicado = false;

        for (CompradorMODEL c : compradores) {
            if (c.getNome().equalsIgnoreCase(nome)) {
                nomeDuplicado = true;
            }
            if (c.getTelefone().equals(telefone)) {
                telefoneDuplicado = true;
            }
            if (c.getCPF().equals(CPF)) {
                cpfDuplicado = true;
            }
        }

        if (nomeDuplicado) {
            System.out.println("Já existe um comprador com esse nome.");
        }
        if (telefoneDuplicado) {
            System.out.println("Já existe um comprador com esse telefone.");
        }
        if (cpfDuplicado) {
            System.out.println("Já existe um comprador com esse CPF.");
        }

        // Se houver qualquer duplicidade, aborta o cadastro
        if (nomeDuplicado || telefoneDuplicado || cpfDuplicado) {
            System.out.println("Cadastro cancelado devido a dados duplicados.");
            return;
        }

        CompradorMODEL comprador = new CompradorMODEL(nome, telefone, CPF);
        compradorRepository.salvar(comprador);
        System.out.println("Comprador cadastrado com sucesso!");
    }

    public void listarCompradores() {
        List<CompradorMODEL> compradores = compradorRepository.listarTodos();
        if (compradores.isEmpty()) {
            System.out.println("Nenhum comprador cadastrado.");
            return;
        }

        for (CompradorMODEL c : compradores) {
            System.out.println("ID: " + c.getId() + " | Nome: " + c.getNome() + " | Telefone: " + c.getTelefone() +" | CPF: " + c.getCPF() );
        }
    }

    public void atualizarComprador() {
        List<CompradorMODEL> compradores = compradorRepository.listarTodos();
        if (compradores.isEmpty()) {
            System.out.println("Não há compradores cadastrados.");
            System.out.print("Deseja cadastrar um novo comprador? (s/n): ");
            String opcao = scanner.nextLine();
            if (opcao.equalsIgnoreCase("s")) {
                criarComprador();
            }
            return;
        }

        listarCompradores();
        System.out.print("Digite o ID do comprador a ser atualizado: ");
        Long id = Long.parseLong(scanner.nextLine());

        CompradorMODEL comprador = compradorRepository.buscarPorId(id);
        if (comprador == null) {
            System.out.println("Comprador não encontrado.");
            return;
        }

        System.out.print("Novo nome: ");
        comprador.setNome(scanner.nextLine());
        System.out.print("Novo telefone: ");
        comprador.setTelefone(scanner.nextLine());

        compradorRepository.atualizar(comprador);
        System.out.println("Comprador atualizado com sucesso!");
    }

    public void deletarComprador() {
        List<CompradorMODEL> compradores = compradorRepository.listarTodos();
        if (compradores.isEmpty()) {
            System.out.println("Não há compradores cadastrados.");
            return;
        }

        listarCompradores();
        System.out.print("Digite o ID do comprador a ser removido: ");
        Long id = Long.parseLong(scanner.nextLine());

        CompradorMODEL comprador = compradorRepository.buscarPorId(id);
        if (comprador == null) {
            System.out.println("Comprador não encontrado.");
            return;
        }

        compradorRepository.deletar(id);
        System.out.println("Comprador removido com sucesso!");
    }

    public CompradorMODEL buscarCompradorPorCPF() {
        while (true) {
            System.out.print("Digite o CPF do comprador (ou digite 'cancelar' para sair): ");
            String entrada = scanner.nextLine().trim();

            if (entrada.equalsIgnoreCase("cancelar")) {
                System.out.print("Tem certeza que quer cancelar a operação? (s/n): ");
                String confirmacao = scanner.nextLine().trim();
                if (confirmacao.equalsIgnoreCase("s")) {
                    System.out.println("Operação cancelada.");
                    return null;
                } else {
                    continue;
                }
            }

            CompradorMODEL comprador = compradorRepository.buscarPorCPF(entrada);
            if (comprador == null) {
                System.out.println("CPF não encontrado. Tente novamente.");
            } else {
                System.out.println("Comprador encontrado: " + comprador.getNome() + " | Telefone: " + comprador.getTelefone());
                return comprador;
            }
        }
    }
}
