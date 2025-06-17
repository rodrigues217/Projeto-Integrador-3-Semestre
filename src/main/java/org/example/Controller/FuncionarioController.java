package org.example.Controller;

import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Service.FuncionarioService;

import java.util.List;


public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController() {
        this.funcionarioService = new FuncionarioService();
    }

    public void criarFuncionario(String nome,
                                 String cpf,
                                 String endereco,
                                 String telefone,
                                 Long usuarioId,
                                 Long setorId) throws Exception {
        funcionarioService.criarFuncionario(nome, cpf, endereco, telefone, usuarioId, setorId);
    }

    /**
     * Lista todos os funcionários cadastrados.
     */
    public List<FuncionarioMODEL> listarFuncionarios() {
        return funcionarioService.listarFuncionarios();
    }

    public FuncionarioMODEL buscarFuncionarioPorCPF(String cpf) throws Exception {
        return funcionarioService.buscarFuncionarioPorCPF(cpf);
    }


    public void deletarFuncionario(String cpf) throws Exception {
        funcionarioService.deletarFuncionario(cpf);
    }


    public void atualizarFuncionario(Long id,
                                     String novoNome,
                                     String novoCpf,
                                     String novoEndereco,
                                     String novoTelefone) throws Exception {
        funcionarioService.atualizarFuncionario(id, novoNome, novoCpf, novoEndereco, novoTelefone);
    }


    public void trocarUsuarioDeFuncionario(Long funcId, Long usuarioId) throws Exception {
        funcionarioService.trocarUsuarioDeFuncionario(funcId, usuarioId);
    }


    public void trocarSetorDeFuncionario(Long funcId, Long setorId) throws Exception {
        funcionarioService.trocarSetorDeFuncionario(funcId, setorId);
    }
}
