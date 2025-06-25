package org.example.Controller;

import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Service.CompradorService;

import java.util.List;

public class CompradorController {

    private final CompradorService compradorService;

    public CompradorController() {
        this.compradorService = new CompradorService();
    }

    public CompradorMODEL criarComprador(String nome, String telefone, String cpf, String email, String endereco) throws Exception {
        return compradorService.criarComprador(nome, telefone, cpf, email, endereco);
    }

    public List<CompradorMODEL> listarTodosCompradores() {
        return compradorService.listarTodosCompradores();
    }

    public CompradorMODEL atualizarComprador(Long id, String novoNome, String novoTelefone, String novoCpf, String novoEmail, String novoEndereco) throws Exception {
        return compradorService.atualizarComprador(id, novoNome, novoTelefone, novoCpf, novoEmail, novoEndereco);
    }

    public void removerComprador(Long id) throws Exception {
        compradorService.removerComprador(id);
    }

    public CompradorMODEL buscarCompradorPorId(Long id) {
        return compradorService.buscarCompradorPorId(id);
    }

    public CompradorMODEL buscarCompradorPorCPF(String cpf) {
        return compradorService.buscarCompradorPorCPF(cpf);
    }

    public boolean verificarCpfExistente(String cpf, Long idAtual) {
        return compradorService.verificarCpfExistente(cpf, idAtual);

    }
}
