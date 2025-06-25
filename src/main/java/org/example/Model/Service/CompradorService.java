package org.example.Model.Service;

import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Repository.CompradorRepository;

import java.util.List;

public class CompradorService {
    private CompradorRepository compradorRepository = new CompradorRepository();

    public CompradorMODEL criarComprador(String nome, String telefone, String cpf, String email, String endereco) throws Exception {
        if (compradorRepository.buscarPorCPF(cpf) != null) {
            throw new Exception("Já existe um comprador com esse CPF.");
        }
        CompradorMODEL comprador = new CompradorMODEL(nome, telefone, cpf, email, endereco);
        compradorRepository.salvar(comprador);
        return comprador;
    }

    public List<CompradorMODEL> listarTodosCompradores() {
        return compradorRepository.listarTodos();
    }

    public CompradorMODEL atualizarComprador(Long id, String novoNome, String novoTelefone, String novoCpf, String novoEmail, String novoEndereco) throws Exception {
        CompradorMODEL comprador = compradorRepository.buscarPorId(id);
        if (comprador == null) {
            throw new Exception("Comprador não encontrado.");
        }

        // Verifica se o novo CPF já existe para outro comprador
        CompradorMODEL compradorComNovoCpf = compradorRepository.buscarPorCPF(novoCpf);
        if (compradorComNovoCpf != null && !compradorComNovoCpf.getId().equals(id)) {
            throw new Exception("Já existe outro comprador com este CPF.");
        }

        comprador.setNome(novoNome);
        comprador.setTelefone(novoTelefone);
        comprador.setCPF(novoCpf);
        comprador.setEmail(novoEmail);
        comprador.setEndereco(novoEndereco);

        compradorRepository.atualizar(comprador);
        return comprador;
    }

    public void removerComprador(Long id) throws Exception {
        CompradorMODEL comprador = compradorRepository.buscarPorId(id);
        if (comprador == null) {
            throw new Exception("Comprador não encontrado.");
        }
        compradorRepository.deletar(id);
    }

    public CompradorMODEL buscarCompradorPorId(Long id) {
        return compradorRepository.buscarPorId(id);
    }

    public CompradorMODEL buscarCompradorPorCPF(String cpf) {
        return compradorRepository.buscarPorCPF(cpf);
    }

    public boolean verificarCpfExistente(String cpf, Long idAtual) {
        CompradorMODEL compradorExistente = compradorRepository.buscarPorCPF(cpf);
        if (compradorExistente != null) {
            return idAtual == null || !compradorExistente.getId().equals(idAtual);
        }
        return false;
    }
}