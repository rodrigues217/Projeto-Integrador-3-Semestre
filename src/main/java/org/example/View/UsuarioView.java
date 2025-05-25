package org.example.View;

import java.util.Scanner;

public class UsuarioView {

    private final Scanner scanner = new Scanner(System.in);

    public String pedirLogin() {
        System.out.print("Digite o login: ");
        return scanner.nextLine();
    }

    public String pedirSenha() {
        System.out.print("Digite a senha: ");
        return scanner.nextLine();
    }

    public Long pedirId() {
        System.out.print("Digite o ID do usuário: ");
        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return null;
        }
    }

    public String pedirConfirmacao(String mensagem) {
        System.out.print(mensagem + " (sim/nao): ");
        return scanner.nextLine();
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void mostrarUsuarios(java.util.List<org.example.Model.UsuarioMODEL> usuarios) {
        System.out.println("\n--- LISTA DE USUÁRIOS ---");
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            for (var usuario : usuarios) {
                System.out.println("ID: " + usuario.getId() + " | Login: " + usuario.getLogin());
            }
        }
    }
}
