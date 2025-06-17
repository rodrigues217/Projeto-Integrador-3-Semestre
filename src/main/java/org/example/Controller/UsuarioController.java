package org.example.Controller;

import org.example.Model.Service.UsuarioService;

import java.util.Scanner;

public class UsuarioController {
    private final UsuarioService usuarioService = new UsuarioService();

    public void criarUsuario() {
        usuarioService.criarUsuario();
    }

    public void listarUsuarios() {
        usuarioService.listarUsuarios();
    }

    public void atualizarUsuario() {
        usuarioService.atualizarUsuario();
    }

    public void deletarUsuario() {
        usuarioService.deletarUsuario();
    }

    public void fazerLogin(Scanner scanner) {
        usuarioService.fazerLogin(scanner);
    }
}
