package org.example.View;

import org.example.Controller.UsuarioController;
import org.example.Model.UsuarioMODEL;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UsuarioController usuarioController = new UsuarioController();

        // Primeira etapa: Login
        UsuarioMODEL usuarioLogado = usuarioController.fazerLogin(scanner);

        // Após login, redirecionar para o menu principal
        VendaView.mostrarMenuPrincipal(scanner, usuarioLogado);
    }
}
