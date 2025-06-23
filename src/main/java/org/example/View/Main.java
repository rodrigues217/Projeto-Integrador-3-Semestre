/**package org.example.View;

import org.example.Model.Service.UsuarioService;
import org.example.Model.Entity.UsuarioMODEL;
import org.example.Model.Entity.ProdutosMODEL;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UsuarioService usuarioService = new UsuarioService();

        // Primeira etapa: Login
        UsuarioMODEL usuarioLogado = usuarioService.fazerLogin(scanner);

        // Após login, redireciona para o menu principal (venda no console)
        View view = new View();
        view.menuInicial();
    }
}
**/