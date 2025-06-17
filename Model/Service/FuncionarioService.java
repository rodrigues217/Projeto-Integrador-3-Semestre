package org.example.Model.Service;

import org.example.Model.Entity.AuditoriaVendaMODEL;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.SetorMODEL;
import org.example.Model.Entity.UsuarioMODEL;
import org.example.Model.Repository.AuditoriaVendaRepository;
import org.example.Model.Repository.FuncionarioRepository;
import org.example.Model.Repository.SetorRepository;
import org.example.Model.Repository.UsuarioRepository;

import java.util.List;

public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository = new FuncionarioRepository();
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final SetorRepository setorRepository = new SetorRepository();
    private final AuditoriaVendaRepository auditoriaVendaRepository = new AuditoriaVendaRepository();

    public void criarFuncionario(String nome, String CPF, String endereco, String telefone, Long usuarioId, Long setorId) throws Exception {
        List<UsuarioMODEL> usuarios = usuarioRepository.listarTodos();
        List<SetorMODEL> setores = setorRepository.listarTodos();

        if (usuarios.isEmpty()) {
            throw new Exception("Erro: Não há usuários cadastrados.");
        }
        if (setores.isEmpty()) {
            throw new Exception("Erro: Não há setores cadastrados.");
        }

        UsuarioMODEL usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new Exception("Usuário não encontrado.");
        }

        SetorMODEL setor = setorRepository.buscarPorId(setorId);
        if (setor == null) {
            throw new Exception("Setor não encontrado.");
        }

        FuncionarioMODEL funcionario = new FuncionarioMODEL(nome, CPF, endereco, telefone, setor, usuario);
        funcionarioRepository.salvar(funcionario);
    }

    public void trocarUsuarioDeFuncionario(Long funcId, Long usuarioId) throws Exception {
        FuncionarioMODEL funcionario = funcionarioRepository.buscarPorId(funcId);
        if (funcionario == null) {
            throw new Exception("Funcionário não encontrado.");
        }

        UsuarioMODEL novoUsuario = usuarioRepository.buscarPorId(usuarioId);
        if (novoUsuario == null) {
            throw new Exception("Usuário não encontrado.");
        }

        funcionario.setUsuario(novoUsuario);
        funcionarioRepository.atualizar(funcionario);
    }

    public void trocarSetorDeFuncionario(Long funcId, Long setorId) throws Exception {
        FuncionarioMODEL funcionario = funcionarioRepository.buscarPorId(funcId);
        if (funcionario == null) {
            throw new Exception("Funcionário não encontrado.");
        }

        SetorMODEL novoSetor = setorRepository.buscarPorId(setorId);
        if (novoSetor == null) {
            throw new Exception("Setor não encontrado.");
        }

        funcionario.setSetor(novoSetor);
        funcionarioRepository.atualizar(funcionario);
    }

    public void atualizarFuncionario(Long id, String novoNome, String novoCPF, String novoEndereco, String novoTelefone) throws Exception {
        FuncionarioMODEL funcionario = funcionarioRepository.buscarPorId(id);
        if (funcionario == null) {
            throw new Exception("Funcionário não encontrado.");
        }

        funcionario.setNome(novoNome);
        funcionario.setCPF(novoCPF);
        funcionario.setEndereco(novoEndereco);
        funcionario.setTelefone(novoTelefone);

        funcionarioRepository.atualizar(funcionario);
    }

    public List<FuncionarioMODEL> listarFuncionarios() {
        return funcionarioRepository.listarTodos();
    }

    public FuncionarioMODEL buscarFuncionarioPorCPF(String cpf) throws Exception {
        FuncionarioMODEL funcionario = funcionarioRepository.buscarFuncionarioPorCPF(cpf);
        if (funcionario == null) {
            throw new Exception("CPF inválido. Nenhum funcionário encontrado com esse CPF.");
        }
        return funcionario;
    }

    public void deletarFuncionario(String cpf) throws Exception {
        FuncionarioMODEL funcionario = funcionarioRepository.buscarFuncionarioPorCPF(cpf);

        if (funcionario == null) {
            throw new Exception("Funcionário não encontrado.");
        }

        List<AuditoriaVendaMODEL> auditorias = auditoriaVendaRepository.buscarPorFuncionarioCPF(cpf);

        if (!auditorias.isEmpty()) {
            throw new Exception("Este funcionário está vinculado a registros de venda e não pode ser deletado. SUGESTÃO: Desative esse funcionário movendo-o para o setor 'Funcionários Desativados'.");
        }

        funcionarioRepository.deletar(funcionario.getId());
    }
}


