package org.example.Model.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.Model.Entity.AuditoriaVendaMODEL;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.SetorMODEL;
import org.example.Model.Entity.UsuarioMODEL;
import org.example.Model.Repository.AuditoriaVendaRepository;
import org.example.Model.Repository.FuncionarioRepository;
import org.example.Model.Repository.SetorRepository;
import org.example.Model.Repository.UsuarioRepository;
import org.example.Model.Util.HibernateUtil; // Import HibernateUtil

import java.util.Collections; // Import Collections for emptyList
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository = new FuncionarioRepository();
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final SetorRepository setorRepository = new SetorRepository();
    private final AuditoriaVendaRepository auditoriaVendaRepository = new AuditoriaVendaRepository();
    // Scanner é mantido para os métodos antigos, mas não usado nos novos métodos Swing
    private final Scanner scanner = new Scanner(System.in);

    // --- Métodos para Swing ---

    /**
     * Busca um funcionário associado a um determinado usuário.
     * Útil para obter o funcionário logado a partir do usuário autenticado.
     * @param usuario O UsuarioMODEL autenticado.
     * @return Optional contendo o FuncionarioMODEL se encontrado, Optional.empty() caso contrário.
     */
    public Optional<FuncionarioMODEL> buscarFuncionarioPorUsuario(UsuarioMODEL usuario) {
        if (usuario == null || usuario.getId() == null) {
            return Optional.empty();
        }
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            FuncionarioMODEL funcionario = em.createQuery(
                            "SELECT f FROM Funcionario f WHERE f.usuario.id = :usuarioId", FuncionarioMODEL.class)
                    .setParameter("usuarioId", usuario.getId())
                    .setMaxResults(1) // Garante que apenas um resultado seja retornado
                    .getSingleResult();
            return Optional.of(funcionario);
        } catch (NoResultException e) {
            // É normal não encontrar um funcionário para um usuário (ex: usuário ADM sem vínculo direto)
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Erro ao buscar funcionário por usuário: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Lista todos os funcionários cadastrados.
     * @return Uma lista de FuncionarioMODEL, ou lista vazia se ocorrer erro.
     */
    public List<FuncionarioMODEL> listarFuncionarios() {
        try {
            return funcionarioRepository.listarTodos();
        } catch (Exception e) {
            System.err.println("Erro ao listar todos os funcionários: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); // Retorna lista vazia em caso de erro
        }
    }

    // --- Métodos existentes (baseados em Scanner) ---
    // Manter por compatibilidade ou refatorar se a interface de console for removida

    public void criarFuncionario() {
        List<UsuarioMODEL> usuarios = usuarioRepository.listarTodos();
        List<SetorMODEL> setores = setorRepository.listarTodos();

        if (usuarios.isEmpty() && setores.isEmpty()) {
            System.out.println("Erro: Não há usuários e setores cadastrados. Redirecionando para o menu principal.");
            return;
        } else if (usuarios.isEmpty()) {
            System.out.println("Erro: Não há usuários cadastrados. Redirecionando para o menu principal.");
            return;
        } else if (setores.isEmpty()) {
            System.out.println("Erro: Não há setores cadastrados. Redirecionando para o menu principal.");
            return;
        }

        System.out.print("Nome do funcionário: ");
        String nome = scanner.nextLine();

        System.out.print("CPF: ");
        String CPF = scanner.nextLine();

        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        // Selecionar usuário
        UsuarioMODEL usuario = null;
        while (usuario == null) {
            System.out.println("Usuários disponíveis:");
            for (UsuarioMODEL u : usuarios) {
                System.out.println("ID: " + u.getId() + " | Login: " + u.getLogin());
            }
            System.out.print("Digite o ID do usuário a ser associado (ou 0 para cancelar): ");
            Long usuarioId;
            try {
                usuarioId = Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
                continue;
            }
            if (usuarioId == 0L) {
                System.out.print("Tem certeza que deseja cancelar a criação? (s/n): ");
                if (scanner.nextLine().equalsIgnoreCase("s")) return;
                else continue;
            }
            usuario = usuarioRepository.buscarPorId(usuarioId);
            if (usuario == null) {
                System.out.println("Usuário não encontrado. Tente novamente.");
            }
        }

        // Selecionar setor
        SetorMODEL setor = null;
        while (setor == null) {
            System.out.println("Setores disponíveis:");
            for (SetorMODEL s : setores) {
                System.out.println("ID: " + s.getId() + " | Nome: " + s.getNome());
            }
            System.out.print("Digite o ID do setor a ser associado (ou 0 para cancelar): ");
            Long setorId;
            try {
                setorId = Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
                continue;
            }
            if (setorId == 0L) {
                System.out.print("Tem certeza que deseja cancelar a criação? (s/n): ");
                if (scanner.nextLine().equalsIgnoreCase("s")) return;
                else continue;
            }
            setor = setorRepository.buscarPorId(setorId);
            if (setor == null) {
                System.out.println("Setor não encontrado. Tente novamente.");
            }
        }

        FuncionarioMODEL funcionario = new FuncionarioMODEL(nome, CPF, endereco, telefone, setor, usuario);
        funcionarioRepository.salvar(funcionario);
        System.out.println("Funcionário criado com sucesso!");
    }

    public void trocarUsuarioDeFuncionario() {
        List<FuncionarioMODEL> funcionarios = funcionarioRepository.listarTodos();
        if (funcionarios.isEmpty()) {
            System.out.println("Não há funcionários cadastrados.");
            return;
        }

        System.out.println("Funcionários:");
        for (FuncionarioMODEL f : funcionarios) {
            System.out.println("ID: " + f.getId() + " | Nome: " + f.getNome());
        }

        Long funcId = null;
        while (true) {
            try {
                System.out.print("Digite o ID do funcionário para alterar o usuário (ou 'cancelar'): ");
                String entrada = scanner.nextLine().trim();

                if (entrada.equalsIgnoreCase("cancelar")) {
                    System.out.println("Operação cancelada.");
                    return;
                }

                funcId = Long.parseLong(entrada);
                break;

            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número válido para o ID.");
            }
        }

        FuncionarioMODEL funcionario = funcionarioRepository.buscarPorId(funcId);
        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        List<UsuarioMODEL> usuarios = usuarioRepository.listarTodos();
        if (usuarios.isEmpty()) {
            System.out.println("Não há usuários disponíveis.");
            return;
        }

        System.out.println("Usuários disponíveis:");
        for (UsuarioMODEL u : usuarios) {
            System.out.println("ID: " + u.getId() + " | Login: " + u.getLogin());
        }

        Long usuarioId = null;
        while (true) {
            try {
                System.out.print("Digite o ID do novo usuário (ou 'cancelar'): ");
                String entrada = scanner.nextLine().trim();

                if (entrada.equalsIgnoreCase("cancelar")) {
                    System.out.println("Operação cancelada.");
                    return;
                }

                usuarioId = Long.parseLong(entrada);
                break;

            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número válido para o ID.");
            }
        }

        UsuarioMODEL novoUsuario = usuarioRepository.buscarPorId(usuarioId);
        if (novoUsuario == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        funcionario.setUsuario(novoUsuario);
        funcionarioRepository.atualizar(funcionario);
        System.out.println("Usuário do funcionário atualizado com sucesso!");
    }


    public void trocarSetorDeFuncionario() {
        List<FuncionarioMODEL> funcionarios = funcionarioRepository.listarTodos();
        if (funcionarios.isEmpty()) {
            System.out.println("Não há funcionários cadastrados.");
            return;
        }

        System.out.println("Funcionários:");
        for (FuncionarioMODEL f : funcionarios) {
            System.out.println("ID: " + f.getId() + " | Nome: " + f.getNome());
        }

        Long funcId = null;
        while (true) {
            try {
                System.out.print("Digite o ID do funcionário para alterar o setor (ou 'cancelar'): ");
                String entrada = scanner.nextLine().trim();

                if (entrada.equalsIgnoreCase("cancelar")) {
                    System.out.println("Operação cancelada.");
                    return;
                }

                funcId = Long.parseLong(entrada);
                break;

            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número válido para o ID.");
            }
        }

        FuncionarioMODEL funcionario = funcionarioRepository.buscarPorId(funcId);
        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        List<SetorMODEL> setores = setorRepository.listarTodos();
        if (setores.isEmpty()) {
            System.out.println("Não há setores disponíveis.");
            return;
        }

        System.out.println("Setores disponíveis:");
        for (SetorMODEL s : setores) {
            System.out.println("ID: " + s.getId() + " | Nome: " + s.getNome());
        }

        Long setorId = null;
        while (true) {
            try {
                System.out.print("Digite o ID do novo setor (ou 'cancelar'): ");
                String entrada = scanner.nextLine().trim();

                if (entrada.equalsIgnoreCase("cancelar")) {
                    System.out.println("Operação cancelada.");
                    return;
                }

                setorId = Long.parseLong(entrada);
                break;

            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número válido para o ID.");
            }
        }

        SetorMODEL novoSetor = setorRepository.buscarPorId(setorId);
        if (novoSetor == null) {
            System.out.println("Setor não encontrado.");
            return;
        }

        funcionario.setSetor(novoSetor);
        funcionarioRepository.atualizar(funcionario);
        System.out.println("Setor do funcionário atualizado com sucesso!");
    }


    public void atualizarFuncionario() {
        List<FuncionarioMODEL> funcionarios = funcionarioRepository.listarTodos();
        if (funcionarios.isEmpty()) {
            System.out.println("Não há funcionários cadastrados.");
            return;
        }

        System.out.println("Funcionários:");
        for (FuncionarioMODEL f : funcionarios) {
            System.out.println("ID: " + f.getId() + " | Nome: " + f.getNome());
        }

        System.out.print("Digite o ID do funcionário que deseja atualizar: ");
        Long id;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }
        FuncionarioMODEL funcionario = funcionarioRepository.buscarPorId(id);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        System.out.print("Novo nome (atual: " + funcionario.getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isBlank()) {
            funcionario.setNome(novoNome);
        }

        System.out.print("Novo CPF (atual: " + funcionario.getCPF() + "): ");
        String novoCPF = scanner.nextLine();
        if (!novoCPF.isBlank()) {
            funcionario.setCPF(novoCPF);
        }

        System.out.print("Novo endereço (atual: " + funcionario.getEndereco() + "): ");
        String novoEndereco = scanner.nextLine();
        if (!novoEndereco.isBlank()) {
            funcionario.setEndereco(novoEndereco);
        }

        System.out.print("Novo telefone (atual: " + funcionario.getTelefone() + "): ");
        String novoTelefone = scanner.nextLine();
        if (!novoTelefone.isBlank()) {
            funcionario.setTelefone(novoTelefone);
        }

        funcionarioRepository.atualizar(funcionario);
        System.out.println("Funcionário atualizado com sucesso!");
    }

    // Renomeado para evitar conflito com o novo método Swing
    public void listarFuncionariosConsole() {
        List<FuncionarioMODEL> funcionarios = funcionarioRepository.listarTodos();
        if (funcionarios.isEmpty()) {
            System.out.println("Não há funcionários cadastrados.");
            return;
        }

        System.out.println("\n--- Lista de funcionários ---");
        for (FuncionarioMODEL f : funcionarios) {
            String setor = (f.getSetor() != null) ? f.getSetor().getNome() : "Nenhum";
            String usuario = (f.getUsuario() != null) ? f.getUsuario().getLogin() : "Nenhum";

            System.out.println("ID: " + f.getId());
            System.out.println("Nome: " + f.getNome());
            System.out.println("CPF: " + f.getCPF());
            System.out.println("Endereço: " + f.getEndereco());
            System.out.println("Telefone: " + f.getTelefone());
            System.out.println("Setor: " + setor);
            System.out.println("Usuário: " + usuario);
            System.out.println("------------------------");
        }
    }

    public FuncionarioMODEL buscarFuncionarioPorCPF() {
        while (true) {
            System.out.print("Digite o CPF do funcionário (ou digite 'cancelar' para sair): ");
            String cpf = scanner.nextLine().trim();

            if (cpf.equalsIgnoreCase("cancelar")) {
                System.out.print("Tem certeza que deseja cancelar a operação? (s/n): ");
                String confirmacao = scanner.nextLine().trim();
                if (confirmacao.equalsIgnoreCase("s")) {
                    System.out.println("Operação cancelada.");
                    return null;
                } else {
                    continue;
                }
            }

            FuncionarioMODEL funcionario = funcionarioRepository.buscarFuncionarioPorCPF(cpf);

            if (funcionario == null) {
                System.out.println("CPF inválido. Nenhum funcionário encontrado com esse CPF.");
            } else {
                System.out.println("ID: " + funcionario.getId());
                System.out.println("Nome: " + funcionario.getNome());
                System.out.println("CPF: " + funcionario.getCPF());
                System.out.println("Endereço: " + funcionario.getEndereco());
                System.out.println("Telefone: " + funcionario.getTelefone());
                // Corrigido para mostrar nome do setor e login do usuário
                System.out.println("Setor: " + (funcionario.getSetor() != null ? funcionario.getSetor().getNome() : "Nenhum"));
                System.out.println("Usuário: " + (funcionario.getUsuario() != null ? funcionario.getUsuario().getLogin() : "Nenhum"));
                System.out.println("------------------------");

                return funcionario;
            }
        }
    }

    public void deletarFuncionario(Scanner scanner) {
        List<FuncionarioMODEL> funcionarios = funcionarioRepository.listarTodos();

        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }

        System.out.print("Digite o CPF do funcionário que deseja deletar: ");
        String cpf = scanner.nextLine();

        FuncionarioMODEL funcionario = funcionarioRepository.buscarFuncionarioPorCPF(cpf);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        // Verificar se o funcionário está vinculado a alguma auditoria
        List<AuditoriaVendaMODEL> auditorias = auditoriaVendaRepository.buscarPorFuncionarioCPF(cpf);

        if (!auditorias.isEmpty()) {
            System.out.println("Este funcionário está vinculado a registros de venda e não pode ser deletado.");
            System.out.println("SUGESTÃO: Desative esse funcionário movendo-o para o setor 'Funcionários Desativados'.");
            return;
        }

        System.out.print("Tem certeza que deseja deletar o funcionário " + funcionario.getNome() + "? (s/n): ");
        String confirmacao = scanner.nextLine();
        if (confirmacao.equalsIgnoreCase("s")) {
            funcionarioRepository.deletar(funcionario.getId());
            System.out.println("Funcionário deletado com sucesso.");
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}

