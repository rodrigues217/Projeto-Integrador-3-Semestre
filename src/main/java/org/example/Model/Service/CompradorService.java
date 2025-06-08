package org.example.Model.Service;

import jakarta.persistence.EntityManager;
import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Repository.CompradorRepository;
import org.example.Model.Util.HibernateUtil; // Import HibernateUtil

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CompradorService {
    private CompradorRepository compradorRepository = new CompradorRepository();
    // Scanner é mantido para os métodos antigos, mas não usado nos novos métodos Swing
    private Scanner scanner = new Scanner(System.in);

    // --- Métodos para Swing ---

    /**
     * Busca um comprador pelo CPF para uso em interfaces gráficas.
     * @param cpf O CPF a ser buscado.
     * @return Optional contendo o CompradorMODEL se encontrado, Optional.empty() caso contrário.
     */
    public Optional<CompradorMODEL> buscarPorCPFSwing(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return Optional.empty();
        }
        // Delega a busca para o repositório e encapsula em Optional
        return Optional.ofNullable(compradorRepository.buscarPorCPF(cpf));
    }

    /**
     * Cria um novo comprador a partir de dados fornecidos (ex: de uma GUI).
     * Realiza validação de duplicidade antes de salvar.
     * @param nome Nome do comprador.
     * @param telefone Telefone do comprador.
     * @param cpf CPF do comprador.
     * @return Optional contendo o CompradorMODEL criado se sucesso, Optional.empty() se falhar (ex: dados duplicados).
     */
    public Optional<CompradorMODEL> criarCompradorSwing(String nome, String telefone, String cpf) {
        if (nome == null || nome.isBlank() || telefone == null || telefone.isBlank() || cpf == null || cpf.isBlank()) {
            System.err.println("Erro ao criar comprador: Dados inválidos (nome, telefone ou CPF em branco).");
            return Optional.empty(); // Retorna vazio se dados essenciais estiverem faltando
        }

        // Validação de duplicidade (sem usar Scanner)
        List<CompradorMODEL> compradores = compradorRepository.listarTodos();
        boolean cpfDuplicado = compradores.stream().anyMatch(c -> c.getCPF().equalsIgnoreCase(cpf));
        // Poderia adicionar validação de nome/telefone se a regra de negócio exigir unicidade

        if (cpfDuplicado) {
            System.err.println("Erro ao criar comprador: CPF já cadastrado.");
            return Optional.empty(); // Retorna vazio se CPF duplicado
        }

        CompradorMODEL novoComprador = new CompradorMODEL(nome, telefone, cpf);
        try {
            compradorRepository.salvar(novoComprador);
            System.out.println("Comprador cadastrado via Swing: " + novoComprador.getNome());
            return Optional.of(novoComprador); // Retorna o comprador criado
        } catch (Exception e) {
            System.err.println("Erro ao salvar novo comprador: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty(); // Retorna vazio em caso de erro ao salvar
        }
    }

    /**
     * Lista todos os compradores.
     * @return Lista de compradores.
     */
    public List<CompradorMODEL> listarTodos() {
        return compradorRepository.listarTodos();
    }

    // --- Métodos existentes (baseados em Scanner) ---
    // Manter por compatibilidade ou refatorar se a interface de console for removida

    public void criarComprador() {
        System.out.print("Nome do comprador: ");
        String nome = scanner.nextLine();

        System.out.print("Telefone do comprador: ");
        String telefone = scanner.nextLine();

        System.out.print("CPF do comprador: ");
        String CPF = scanner.nextLine();

        // Chama o método refatorado que contém a lógica de validação e salvamento
        Optional<CompradorMODEL> resultado = criarCompradorSwing(nome, telefone, CPF);

        if (resultado.isPresent()) {
            System.out.println("Comprador cadastrado com sucesso!");
        } else {
            // A mensagem de erro específica (CPF duplicado, etc.) já foi impressa em criarCompradorSwing
            System.out.println("Falha ao cadastrar comprador.");
        }
    }

    public void listarCompradores() {
        List<CompradorMODEL> compradores = compradorRepository.listarTodos();
        if (compradores.isEmpty()) {
            System.out.println("Nenhum comprador cadastrado.");
            return;
        }

        System.out.println("\n--- Lista de Compradores ---");
        for (CompradorMODEL c : compradores) {
            System.out.println("ID: " + c.getId() + " | Nome: " + c.getNome() + " | Telefone: " + c.getTelefone() +" | CPF: " + c.getCPF() );
        }
        System.out.println("---------------------------");
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
        Long id;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        CompradorMODEL comprador = compradorRepository.buscarPorId(id);
        if (comprador == null) {
            System.out.println("Comprador não encontrado.");
            return;
        }

        System.out.print("Novo nome (atual: " + comprador.getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isBlank()) {
            comprador.setNome(novoNome);
        }

        System.out.print("Novo telefone (atual: " + comprador.getTelefone() + "): ");
        String novoTelefone = scanner.nextLine();
        if (!novoTelefone.isBlank()) {
            comprador.setTelefone(novoTelefone);
        }
        // Não permite alterar CPF via este método para simplificar

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
        Long id;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        CompradorMODEL comprador = compradorRepository.buscarPorId(id);
        if (comprador == null) {
            System.out.println("Comprador não encontrado.");
            return;
        }

        // Adicionar verificação se o comprador está em alguma auditoria_vendas antes de deletar?
        // Por enquanto, deleta diretamente.
        System.out.print("Tem certeza que deseja remover o comprador " + comprador.getNome() + "? (s/n): ");
        String confirmacao = scanner.nextLine();
        if (confirmacao.equalsIgnoreCase("s")) {
            compradorRepository.deletar(id);
            System.out.println("Comprador removido com sucesso!");
        } else {
            System.out.println("Operação cancelada.");
        }
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

            // Usa o método refatorado para buscar
            Optional<CompradorMODEL> compradorOpt = buscarPorCPFSwing(entrada);

            if (compradorOpt.isEmpty()) {
                System.out.println("CPF não encontrado. Tente novamente.");
            } else {
                CompradorMODEL comprador = compradorOpt.get();
                System.out.println("Comprador encontrado: " + comprador.getNome() + " | Telefone: " + comprador.getTelefone());
                return comprador;
            }
        }
    }
}

