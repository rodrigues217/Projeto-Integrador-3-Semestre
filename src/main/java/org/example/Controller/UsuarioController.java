package org.example.Controller;

import org.example.Model.Service.UsuarioService;
import org.example.Model.Entity.UsuarioMODEL; // Importar UsuarioMODEL se for retornar
import java.util.List; // Importar List se for usar

import java.util.Scanner; // Manter Scanner se outros métodos ainda o usam

public class UsuarioController {
    private final UsuarioService usuarioService = new UsuarioService();

    // Método ajustado para receber os dados necessários
    // A responsabilidade de obter esses dados (via Scanner, GUI, etc.)
    // fica fora deste método, idealmente em quem o chama (ex: uma View ou Main).
    public void criarUsuario(String login, String senha, String perfilStr) {
        // Chama o método do serviço com os argumentos recebidos
        usuarioService.criarUsuario(login, senha, perfilStr);
    }

    // --- Métodos existentes ---
    // A exibição da lista deve ser tratada por quem chama este método
    public List<UsuarioMODEL> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    // A lógica para obter os dados de atualização (ID, novoLogin, etc.)
    // deve residir em quem chama este método.
    public void atualizarUsuario(Long id, String novoLogin, String novaSenha, String perfilStr) {
        usuarioService.atualizarUsuario(id, novoLogin, novaSenha, perfilStr);
    }

    // A confirmação e obtenção do ID devem ser feitas antes de chamar este método.
    public boolean deletarUsuario(Long id) {
        return usuarioService.deletarUsuario(id);
    }

    // Este método ainda usa Scanner, mantido por compatibilidade com o original
    // mas idealmente a lógica de login via console ficaria fora do service/controller.
    public void fazerLogin(Scanner scanner) {
        usuarioService.fazerLogin(scanner);
    }

    // Método adicional para usar a autenticação Swing (se o controller for usado pela GUI)
    public UsuarioMODEL autenticarViaSwing(String login, String senha) {
        return usuarioService.autenticarViaSwing(login, senha);
    }
}

