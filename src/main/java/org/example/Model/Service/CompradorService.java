package org.example.Model.Service;

import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Repository.CompradorRepository;

public class CompradorService {
    private final CompradorRepository compradorRepository = new CompradorRepository();

    public CompradorMODEL criarComprador(String nome, String telefone) {
        CompradorMODEL comprador = new CompradorMODEL();
        comprador.setNome(nome);
        comprador.setTelefone(telefone);
        compradorRepository.salvar(comprador);
        return comprador;
    }

    public CompradorMODEL buscarPorId(Long id) {
        return compradorRepository.buscarPorId(id);
    }
}