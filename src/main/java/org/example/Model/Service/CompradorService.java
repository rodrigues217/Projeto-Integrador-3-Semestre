package org.example.Model.Service;

import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Repository.CompradorRepository;

import java.util.List;

public class CompradorService {
    private CompradorRepository compradorRepository = new CompradorRepository();

    public void criarComprador(String nome, String telefone, String CPF) throws Exception {
        List<CompradorMODEL> compradores = compradorRepository.listarTodos();

        boolean nomeDuplicado = compradores.stream().anyMatch(c -> c.getNome().equalsIgnoreCase(nome));
        boolean telefoneDuplicado = compradores.stream().anyMatch(c -> c.getTelefone().equals(telefone));
        boolean cpfDuplicado = compradores.stream().anyMatch(c -> c.getCPF().equals(CPF));

        if (nomeDuplicado) {
            throw new Exception("Já existe um comprador com esse nome.");
        }
        if (telefoneDuplicado) {
            throw new Exception("Já existe um comprador com esse telefone.");
        }
        if (cpfDuplicado) {
            throw new Exception("Já existe um comprador com esse CPF.");
        }

        CompradorMODEL comprador = new CompradorMODEL(nome, telefone, CPF);
        compradorRepository.salvar(comprador);
    }

    public List<CompradorMODEL> listarCompradores() {
        return compradorRepository.listarTodos();
    }

    public void atualizarComprador(Long id, String novoNome, String novoTelefone) throws Exception {
        CompradorMODEL comprador = compradorRepository.buscarPorId(id);
        if (comprador == null) {
            throw new Exception("Comprador não encontrado.");
        }

        comprador.setNome(novoNome);
        comprador.setTelefone(novoTelefone);

        compradorRepository.atualizar(comprador);
    }

    public void deletarComprador(Long id) throws Exception {
        CompradorMODEL comprador = compradorRepository.buscarPorId(id);
        if (comprador == null) {
            throw new Exception("Comprador não encontrado.");
        }

        compradorRepository.deletar(id);
    }

    public CompradorMODEL buscarCompradorPorCPF(String CPF) throws Exception {
        CompradorMODEL comprador = compradorRepository.buscarPorCPF(CPF);
        if (comprador == null) {
            throw new Exception("CPF não encontrado.");
        }
        return comprador;
    }
}


