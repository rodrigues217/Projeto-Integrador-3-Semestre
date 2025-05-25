package org.example.Controller;

import org.example.Model.UsuarioMODEL;
import org.example.Model.Repository.UsuarioRepository;
import org.example.View.UsuarioView;

import java.util.List;

public class UsuarioController {

    private final UsuarioRepository repository;
    private final UsuarioView view;

    public UsuarioController(UsuarioRepository repository, UsuarioView view) {
        this.repository = repository;
        this.view = view;
    }

    public void criarUsuario() {
        String login = view.pedirLogin();
        String senha = view.pedirSenha();

        UsuarioMODEL usuario = new UsuarioMODEL();
        usuario.setLogin(login);
        usuario.setSenha(senha);

        repository.criarUsuario(usuario);
        view.mostrarMensagem("Usuário criado com sucesso!");
    }

    public void atualizarUsuario() {
        Long id = view.pedirId();
        if (id == null) return;

        UsuarioMODEL usuario = repository.buscarPorId(id);
        if (usuario == null) {
            view.mostrarMensagem("Usuário não encontrado.");
            return;
        }

        String novoLogin = view.pedirLogin();
        String novaSenha = view.pedirSenha();

        usuario.setLogin(novoLogin);
        usuario.setSenha(novaSenha);

        repository.atualizarUsuario(usuario);
        view.mostrarMensagem("Usuário atualizado com sucesso!");
    }

    public void deletarUsuario() {
        Long id = view.pedirId();
        if (id == null) return;

        UsuarioMODEL usuario = repository.buscarPorId(id);
        if (usuario == null) {
            view.mostrarMensagem("Usuário não encontrado.");
            return;
        }

        String confirmacao = view.pedirConfirmacao("Confirma exclusão do usuário " + usuario.getLogin() + "?");
        repository.deletarUsuario(usuario, confirmacao);
    }

    public void listarUsuarios() {
        List<UsuarioMODEL> usuarios = repository.listarUsuarios(); // vamos ajustar isso abaixo
        view.mostrarUsuarios(usuarios);
    }

    public void autenticarUsuario() {
        String login = view.pedirLogin();
        String senha = view.pedirSenha();

        UsuarioMODEL usuario = repository.autenticarUsuario(login, senha);
        if (usuario != null) {
            view.mostrarMensagem("Autenticação realizada com sucesso! Bem-vindo, " + usuario.getLogin());
        } else {
            view.mostrarMensagem("Login ou senha incorretos.");
        }
    }
}
