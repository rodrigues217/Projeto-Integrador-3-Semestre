package org.example.Controller;

import org.example.Model.Service.CompradorService;

public class CompradorController {
    private CompradorService compradorService = new CompradorService();

    public void criarComprador() {
        compradorService.criarComprador();
    }

    public void listarCompradores() {
        compradorService.listarCompradores();
    }

    public void atualizarComprador() {
        compradorService.atualizarComprador();
    }

    public void deletarComprador() {
        compradorService.deletarComprador();
    }

    public void buscarCompradorPorCPF(){
        compradorService.buscarCompradorPorCPF();
    }
}
