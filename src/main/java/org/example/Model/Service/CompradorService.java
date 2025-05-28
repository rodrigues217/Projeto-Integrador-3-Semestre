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

        // Verificar duplicidade de nome e telefone
        List<CompradorMODEL> compradores = compradorRepository.listarTodos();
        for (CompradorMODEL c : compradores) {
            if (c.getNome().equalsIgnoreCase(nome) && c.getTelefone().equals(telefone)) {
                System.out.println("Já existe um comprador com esse nome e telefone.");
                return;
            }
        }

        CompradorMODEL comprador = new CompradorMODEL(nome, telefone);
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
            System.out.println("ID: " + c.getId() + " | Nome: " + c.getNome() + " | Telefone: " + c.getTelefone());
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
}
