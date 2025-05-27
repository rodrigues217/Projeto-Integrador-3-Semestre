package org.example.Controller;

import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Service.CompradorService;

public class CompradorController {
    private final CompradorService compradorService = new CompradorService();

    public CompradorMODEL criarComprador(String nome, String telefone) {
        return compradorService.criarComprador(nome, telefone);
    }

    public CompradorMODEL buscarComprador(Long id) {
        return compradorService.buscarPorId(id);
    }
}
