package org.example.Controller;

import org.example.Model.Service.FuncionarioService;

import java.util.Scanner;

public class FuncionarioController {

    private final FuncionarioService funcionarioService = new FuncionarioService();

    public void criarFuncionario() {
        funcionarioService.criarFuncionario();
    }

    public void listarFuncionarios() {
        funcionarioService.listarFuncionarios();
    }

    public void buscarFuncionarioPorCPF(){
        funcionarioService.buscarFuncionarioPorCPF();
    }

    public void deletarFuncionario(Scanner scanner){
        funcionarioService.deletarFuncionario(scanner);
    }

    public void atualizarFuncionario() {
        funcionarioService.atualizarFuncionario();
    }

    public void trocarUsuarioDeFuncionario() {
        funcionarioService.trocarUsuarioDeFuncionario();
    }

    public void trocarSetorDeFuncionario() {
        funcionarioService.trocarSetorDeFuncionario();
    }
}
