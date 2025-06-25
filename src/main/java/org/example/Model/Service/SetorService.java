package org.example.Model.Service;

import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.SetorMODEL;
import org.example.Model.Repository.FuncionarioRepository;
import org.example.Model.Repository.SetorRepository;

import java.util.List;

import java.util.stream.Collectors;

public class SetorService {

    private final SetorRepository setorRepository = new SetorRepository();
    private final FuncionarioRepository funcionarioRepository = new FuncionarioRepository();


    public void criarSetor(String nome) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("O nome do setor não pode ser vazio.");
        }

        List<SetorMODEL> setores = setorRepository.listarTodos();
        boolean nomeExistente = setores.stream()
                .anyMatch(setor -> setor.getNome().equalsIgnoreCase(nome));

        if (nomeExistente) {

            throw new Exception("Já existe um setor com esse nome.");

        }

        SetorMODEL novoSetor = new SetorMODEL(nome);
        setorRepository.salvar(novoSetor);

    }

    public List<SetorMODEL> listarSetores() {
        return setorRepository.listarTodos();
    }

    public void atualizarSetor(Long id, String novoNome) throws Exception {
        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new Exception("O novo nome do setor não pode ser vazio.");
        }
        SetorMODEL setor = setorRepository.buscarPorId(id);
        if (setor == null) {
            throw new Exception("Setor não encontrado.");
        }

        List<SetorMODEL> setores = setorRepository.listarTodos();
        boolean nomeExistente = setores.stream()
                .anyMatch(s -> !s.getId().equals(id) && s.getNome().equalsIgnoreCase(novoNome));

        if (nomeExistente) {
            throw new Exception("Já existe outro setor com esse nome.");
        }

        setor.setNome(novoNome);
        setorRepository.atualizar(setor);

    }

    public void deletarSetor(Long id) throws Exception {
        SetorMODEL setor = setorRepository.buscarPorId(id);

        if (setor == null) {
            throw new Exception("Setor não encontrado.");
        }

        List<FuncionarioMODEL> funcionarios = funcionarioRepository.listarTodos().stream()
                .filter(func -> func.getSetor() != null && func.getSetor().getId().equals(id))
                .collect(Collectors.toList());

        if (!funcionarios.isEmpty()) {
            throw new Exception("Não é possível deletar este setor pois existem funcionários vinculados a ele.");
        }

        setorRepository.deletar(id);
    }
}

