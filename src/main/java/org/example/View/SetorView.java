package org.example.View;

import org.example.Model.SetorMODEL;

import java.util.List;
import java.util.Scanner;

public class SetorView {

    private Scanner scanner;

    public SetorView() {
        this.scanner = new Scanner(System.in);
    }

    public String pedirNomeSetor() {
        System.out.print("Digite o nome do setor: ");
        return scanner.nextLine();
    }

    public Long pedirIdSetor() {
        System.out.print("Digite o ID do setor: ");
        while (!scanner.hasNextLong()) {
            System.out.print("Por favor, digite um número válido para o ID: ");
            scanner.next();
        }
        Long id = scanner.nextLong();
        scanner.nextLine(); // Limpar buffer
        return id;
    }

    public String pedirConfirmacao(String mensagem) {
        System.out.print(mensagem + " ");
        return scanner.nextLine();
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void mostrarListaSetores(List<SetorMODEL> setores) {
        System.out.println("\n--- Lista de Setores ---");
        if (setores.isEmpty()) {
            System.out.println("Nenhum setor cadastrado.");
            return;
        }
        for (SetorMODEL setor : setores) {
            System.out.println("ID: " + setor.getId() + " | Nome: " + setor.getNome());
        }
    }
}
