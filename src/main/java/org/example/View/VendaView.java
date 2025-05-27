package org.example.View;

import org.example.Model.Service.VendaService;
import org.example.Model.Entity.UsuarioMODEL;

import java.util.Scanner;

public class VendaView {

    public static void mostrarMenuPrincipal(Scanner scanner, UsuarioMODEL usuario) {
        boolean executando = true;
        VendaService vendaService = new VendaService();

        while (executando) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("Usuário: " + usuario.getLogin() + " | Perfil: " + usuario.getPerfil());
            System.out.println("1. Realizar Venda");
            System.out.println("2. Ver Perfil");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();  // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                    // Solicitar dados para realizar venda
                    System.out.print("ID do Produto: ");
                    Long produtoId = scanner.nextLong();

                    System.out.print("ID do Funcionário: ");
                    Long funcionarioId = scanner.nextLong();

                    System.out.print("Quantidade: ");
                    int quantidade = scanner.nextInt();

                    System.out.print("ID do Comprador (ou 0 para nenhum): ");
                    Long compradorId = scanner.nextLong();
                    if (compradorId == 0) {
                        compradorId = null;
                    }

                    boolean sucesso = vendaService.realizarVenda(produtoId, funcionarioId, quantidade, compradorId);

                    if (sucesso) {
                        System.out.println("Venda realizada com sucesso!");
                    } else {
                        System.out.println("Falha ao realizar a venda.");
                    }
                    break;

                case 2:
                    System.out.println("Login: " + usuario.getLogin());
                    System.out.println("Perfil: " + usuario.getPerfil());
                    System.out.println("Último login: " + usuario.getUltimoLogin());
                    break;

                case 0:
                    System.out.println("Encerrando sistema...");
                    executando = false;
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
