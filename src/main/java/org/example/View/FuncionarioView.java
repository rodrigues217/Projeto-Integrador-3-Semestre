package org.example.View;

import org.example.Model.FuncionarioMODEL;

import java.util.List;
import java.util.Scanner;

public class FuncionarioView {

    private final Scanner scanner;

    public FuncionarioView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibirMenu() {
        System.out.println("\n--- MENU FUNCIONÁRIOS ---");
        System.out.println("1. Cadastrar funcionário");
        System.out.println("2. Listar funcionários");
        System.out.println("3. Atualizar funcionário");
        System.out.println("4. Remover funcionário");
        System.out.println("0. Voltar ao menu principal");
        System.out.print("Escolha uma opção: ");
    }

    public String pedirDadosFuncionario() {
        System.out.print("Digite o nome do funcionário: ");
        return scanner.nextLine();

    }

    public Long pedirIdFuncionario() {
        System.out.print("Digite o ID do funcionário: ");
        return Long.parseLong(scanner.nextLine());
    }

    public String confirmarRemocao(String nome) {
        System.out.print("Tem certeza que deseja remover '" + nome + "'? (s/n): ");
        return scanner.nextLine();
    }

    public void mostrarFuncionarios(List<FuncionarioMODEL> funcionarios) {
        System.out.println("\n*** LISTA DE FUNCIONÁRIOS ***");
        for (FuncionarioMODEL f : funcionarios) {
            System.out.println("ID: " + f.getId() + " - Nome: " + f.getNome());
        }
    }

    public void mostrarMensagem(String msg) {
        System.out.println(msg);
    }
}
