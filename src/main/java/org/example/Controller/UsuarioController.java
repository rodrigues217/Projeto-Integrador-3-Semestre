package org.example.Controller;

import org.example.Model.Entity.UsuarioMODEL;
import org.example.Model.Service.UsuarioService;

import java.util.List;


public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController() {
        this.usuarioService = new UsuarioService();
    }


    public UsuarioMODEL fazerLogin(String login, String senha) throws Exception {
        return usuarioService.fazerLogin(login, senha);
    }


    public void criarUsuario(String login, String senha, String perfil) throws Exception {
        usuarioService.criarUsuario(login, senha, perfil);
    }


    public List<UsuarioMODEL> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }


    public void atualizarUsuario(Long id, String novoLogin, String novaSenha, String perfil) throws Exception {
        usuarioService.atualizarUsuario(id, novoLogin, novaSenha, perfil);
    }

    public void deletarUsuario(Long id) throws Exception {
        usuarioService.deletarUsuario(id);
    }
}
