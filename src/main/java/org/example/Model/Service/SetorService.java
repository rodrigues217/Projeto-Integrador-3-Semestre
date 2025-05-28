package org.example.Model.Service;

import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.SetorMODEL;
import org.example.Model.Repository.FuncionarioRepository;
import org.example.Model.Repository.SetorRepository;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SetorService {

    private final SetorRepository setorRepository = new SetorRepository();
    private final FuncionarioRepository funcionarioRepository = new FuncionarioRepository();
    private final Scanner scanner = new Scanner(System.in);

    public void criarSetor() {
        System.out.print("Digite o nome do novo setor: ");
        String nome = scanner.nextLine();

        List<SetorMODEL> setores = setorRepository.listarTodos();
        boolean nomeExistente = setores.stream()
                .anyMatch(setor -> setor.getNome().equalsIgnoreCase(nome));

        if (nomeExistente) {
            System.out.println("Já existe um setor com esse nome.");
            return;
        }

        SetorMODEL novoSetor = new SetorMODEL(nome);
        setorRepository.salvar(novoSetor);
        System.out.println("Setor criado com sucesso!");
    }

    public void listarSetores() {
        List<SetorMODEL> setores = setorRepository.listarTodos();

        if (setores.isEmpty()) {
            System.out.println("Não existem setores cadastrados.");
            System.out.print("Deseja criar um novo setor agora? (s/n): ");
            String opcao = scanner.nextLine();
            if (opcao.equalsIgnoreCase("s")) {
                criarSetor();
            }
            return;
        }

        for (SetorMODEL setor : setores) {
            System.out.println("Setor ID: " + setor.getId() + " | Nome: " + setor.getNome());

            List<FuncionarioMODEL> funcionarios = funcionarioRepository.listarTodos().stream()
                    .filter(funcionario -> funcionario.getSetor() != null &&
                            funcionario.getSetor().getId().equals(setor.getId()))
                    .collect(Collectors.toList());

            if (funcionarios.isEmpty()) {
                System.out.println("  Nenhum funcionário neste setor.");
            } else {
                funcionarios.forEach(func ->
                        System.out.println("  Funcionário: " + func.getNome() + " | ID: " + func.getId()));
            }
        }
    }

    public void atualizarSetor() {
        List<SetorMODEL> setores = setorRepository.listarTodos();

        if (setores.isEmpty()) {
            System.out.println("Não existem setores cadastrados.");
            System.out.print("Deseja criar um novo setor agora? (s/n): ");
            String opcao = scanner.nextLine();
            if (opcao.equalsIgnoreCase("s")) {
                criarSetor();
            }
            return;
        }

        listarSetores();
        System.out.print("Digite o ID do setor que deseja atualizar: ");
        Long id = Long.parseLong(scanner.nextLine());

        SetorMODEL setor = setorRepository.buscarPorId(id);
        if (setor == null) {
            System.out.println("Setor não encontrado.");
            return;
        }

        System.out.print("Digite o novo nome do setor: ");
        String novoNome = scanner.nextLine();

        boolean nomeExistente = setores.stream()
                .anyMatch(s -> s.getNome().equalsIgnoreCase(novoNome) && !s.getId().equals(id));

        if (nomeExistente) {
            System.out.println("Já existe outro setor com esse nome.");
            return;
        }

        setor.setNome(novoNome);
        setorRepository.atualizar(setor);
        System.out.println("Setor atualizado com sucesso!");
    }

    public void deletarSetor() {
        List<SetorMODEL> setores = setorRepository.listarTodos();

        if (setores.isEmpty()) {
            System.out.println("Não existem setores cadastrados.");
            return;
        }

        listarSetores();
        System.out.print("Digite o ID do setor que deseja deletar: ");
        Long id = Long.parseLong(scanner.nextLine());

        List<FuncionarioMODEL> funcionarios = funcionarioRepository.listarTodos().stream()
                .filter(func -> func.getSetor() != null && func.getSetor().getId().equals(id))
                .collect(Collectors.toList());

        if (!funcionarios.isEmpty()) {
            System.out.println("Não é possível deletar este setor pois existem funcionários vinculados a ele.");
            return;
        }

        setorRepository.deletar(id);
        System.out.println("Setor deletado com sucesso.");
    }
}
