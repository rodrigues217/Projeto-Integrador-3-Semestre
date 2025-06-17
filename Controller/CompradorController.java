package org.example.Controller;

import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Service.CompradorService;

import java.util.List;

public class CompradorController {

    private final CompradorService compradorService;

    public CompradorController() {
        this.compradorService = new CompradorService();
    }

    public void criarComprador(String nome, String telefone, String cpf) throws Exception {
        compradorService.criarComprador(nome, telefone, cpf);
    }

    public List<CompradorMODEL> listarCompradores() {
        return compradorService.listarCompradores();
    }

    public void atualizarComprador(Long id, String novoNome, String novoTelefone) throws Exception {
        compradorService.atualizarComprador(id, novoNome, novoTelefone);
    }


    public void deletarComprador(Long id) throws Exception {
        compradorService.deletarComprador(id);
    }

    public CompradorMODEL buscarCompradorPorCPF(String cpf) throws Exception {
        return compradorService.buscarCompradorPorCPF(cpf);
    }
}
