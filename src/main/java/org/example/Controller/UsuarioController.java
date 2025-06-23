package org.example.Controller;

import org.example.Model.Entity.UsuarioMODEL;
import org.example.Model.Service.UsuarioService;

import java.util.List;

public class UsuarioController {

    private final UsuarioService usuarioService = new UsuarioService();

    public UsuarioMODEL login(String login, String senha) {
        try {
            return usuarioService.fazerLogin(login, senha);
        } catch (Exception e) {
            System.err.println("Erro ao fazer login: " + e.getMessage());
            return null;
        }
    }

    public void criarUsuario(String login, String senha, String perfil) {
        try {
            usuarioService.criarUsuario(login, senha, perfil);
            System.out.println("Usuário criado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao criar usuário: " + e.getMessage());
        }
    }

    public List<UsuarioMODEL> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    public void atualizarUsuario(Long id, String novoLogin, String novaSenha, String perfil) {
        try {
            usuarioService.atualizarUsuario(id, novoLogin, novaSenha, perfil);
            System.out.println("Usuário atualizado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public void deletarUsuario(Long id) {
        try {
            usuarioService.deletarUsuario(id);
            System.out.println("Usuário deletado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
        }
    }
}
