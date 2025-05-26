/*package org.example.Controller;

import org.example.Model.FuncionarioMODEL;
import org.example.Model.UsuarioMODEL;
import org.example.Model.Repository.FuncionarioRepository;
import org.example.Model.Repository.UsuarioRepository;

public class FuncionarioController {

    private FuncionarioRepository funcionarioRepo;
    private UsuarioRepository usuarioRepo;
    private FuncionarioView funcionarioView;

    public FuncionarioController(FuncionarioRepository funcionarioRepo, UsuarioRepository usuarioRepo, FuncionarioView funcionarioView) {
        this.funcionarioRepo = funcionarioRepo;
        this.usuarioRepo = usuarioRepo;
        this.funcionarioView = funcionarioView;
    }

    public void associarUsuarioAFuncionario() {
        funcionarioView.mostrarMensagem("=== ASSOCIAR USUÁRIO A FUNCIONÁRIO ===");

        long idUsuario = funcionarioView.pedirIdFuncionario();
        UsuarioMODEL usuario = usuarioRepo.buscarPorId(idUsuario);
        if (usuario == null) {
            funcionarioView.mostrarMensagem("Usuário não encontrado.");
            return;
        }

        FuncionarioMODEL funcionario = funcionarioView.pedirDadosFuncionario();
        funcionario.setUsuario(usuario);

        funcionarioRepo.cadastrarFuncionario(funcionario);

        funcionarioView.mostrarMensagem("Funcionário cadastrado com sucesso e associado ao usuário: " + usuario.getLogin());
    }

    public void listarFuncionarios() {
        funcionarioView.mostrarFuncionarios(funcionarioRepo.listarFuncionarios(););
    }

    public void removerFuncionario() {
        long id = funcionarioView.pedirIdFuncionario();
        FuncionarioMODEL funcionario = funcionarioRepo.BuscarFuncionarioPorID(id);
        if (funcionario == null) {
            funcionarioView.mostrarMensagem("Funcionário não encontrado.");
            return;
        }

        String confirmacao = funcionarioView.pedirConfirmacao("Tem certeza que deseja remover o funcionário " + funcionario.getNome() + "? (sim/não)");
        if (confirmacao.equalsIgnoreCase("sim")) {
            funcionarioRepo.removerFuncionario(id);
            funcionarioView.mostrarMensagem("Funcionário removido com sucesso.");
        } else {
            funcionarioView.mostrarMensagem("Operação cancelada.");
        }
    }
}*/
