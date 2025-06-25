package org.example.Model.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.PerfilUsuario;
import org.example.Model.Entity.UsuarioMODEL;
import org.example.Model.Repository.FuncionarioRepository;
import org.example.Model.Repository.UsuarioRepository;
import org.example.Model.Util.HibernateUtil;

import java.time.LocalDateTime;
import java.util.List;


public class UsuarioService {

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final FuncionarioRepository funcionarioRepository = new FuncionarioRepository();


    public UsuarioMODEL fazerLogin(String login, String senha) throws Exception {
        EntityManager em = HibernateUtil.getEntityManager();
        UsuarioMODEL usuarioLogado = null;

        try {

            usuarioLogado = autenticarUsuario(em, login, senha);

            if (usuarioLogado == null) {
                throw new Exception("Credenciais inválidas.");
            } else {
                // Atualiza o último login
                em.getTransaction().begin();
                usuarioLogado.setUltimoLogin(LocalDateTime.now());
                em.merge(usuarioLogado);
                em.getTransaction().commit();
                return usuarioLogado;
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    private UsuarioMODEL autenticarUsuario(EntityManager em, String login, String senha) {
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha", UsuarioMODEL.class)
                    .setParameter("login", login)
                    .setParameter("senha", senha)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void criarUsuario(String login, String senha, String perfilStr) throws Exception {
        List<UsuarioMODEL> usuarios = usuarioRepository.listarTodos();
        for (UsuarioMODEL u : usuarios) {
            if (u.getLogin().equalsIgnoreCase(login)) {
                throw new Exception("Já existe um usuário com esse login.");
            }
        }

        try {
            UsuarioMODEL usuario = new UsuarioMODEL(login, senha, Enum.valueOf(PerfilUsuario.class, perfilStr));
            usuarioRepository.salvar(usuario);
        } catch (IllegalArgumentException e) {
            throw new Exception("Perfil inválido. Use 'ADM', 'GERENTE' ou 'OPERADOR'.");
        }
    }

    public List<UsuarioMODEL> listarUsuarios() {
        return usuarioRepository.listarTodos();
    }

    public void atualizarUsuario(Long id, String novoLogin, String novaSenha, String perfilStr) throws Exception {
        UsuarioMODEL usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            throw new Exception("Usuário não encontrado.");
        }

        List<UsuarioMODEL> usuarios = usuarioRepository.listarTodos();
        boolean loginDuplicado = usuarios.stream()
                .anyMatch(u -> !u.getId().equals(id) && u.getLogin().equalsIgnoreCase(novoLogin));

        if (loginDuplicado) {

            throw new Exception("Já existe outro usuário com esse login.");
        }

        usuario.setLogin(novoLogin);
        usuario.setSenha(novaSenha);

        try {
            usuario.setPerfil(Enum.valueOf(PerfilUsuario.class, perfilStr));
        } catch (IllegalArgumentException e) {

            throw new Exception("Perfil inválido. Atualização cancelada.");
        }

        usuarioRepository.atualizar(usuario);
    }

    public void deletarUsuario(Long id) throws Exception {
        UsuarioMODEL usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            throw new Exception("Usuário não encontrado.");
        }

        FuncionarioMODEL funcionario = funcionarioRepository.buscarPorId(id);
        if (funcionario != null) {

            throw new Exception("Este usuário está vinculado ao funcionário: " + funcionario.getNome() + ". Não é possível deletar um usuário vinculado a um funcionário.");
        }

        usuarioRepository.deletar(id);
    }
}


