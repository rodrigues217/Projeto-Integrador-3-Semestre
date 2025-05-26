package org.example.Model.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.Model.UsuarioMODEL;

import java.util.List;

public class UsuarioRepository {

    private static EntityManager em;

    public UsuarioRepository(EntityManager em) {
        this.em = em;
    }
    public void criarUsuario(UsuarioMODEL usuario) {
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
            System.out.println("Usuário criado com sucesso!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            System.out.println("Erro ao criar usuário.");
        }
    }

    public void atualizarUsuario(UsuarioMODEL usuario) {
        try {
            em.getTransaction().begin();
            em.merge(usuario);  // merge atualiza o objeto no banco
            em.getTransaction().commit();
            System.out.println("Usuário atualizado com sucesso!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            System.out.println("Erro ao atualizar usuário.");
        }
    }

    public void deletarUsuario(UsuarioMODEL usuario, String confirmacao) {
        if (confirmacao != null && confirmacao.equalsIgnoreCase("sim")) {
            try {
                em.getTransaction().begin();
                UsuarioMODEL usuarioGerenciado = em.find(UsuarioMODEL.class, usuario.getId());
                if (usuarioGerenciado != null) {
                    em.remove(usuarioGerenciado);
                    em.getTransaction().commit();
                    System.out.println("Usuário deletado com sucesso!");
                } else {
                    System.out.println("Usuário não encontrado no banco.");
                    em.getTransaction().rollback();
                }
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
                System.out.println("Erro ao deletar usuário.");
            }
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    public List<UsuarioMODEL> listarUsuarios() {
        return em.createQuery("SELECT u FROM UsuarioMODEL u", UsuarioMODEL.class)
                .getResultList();
    }


     public UsuarioMODEL buscarPorId(Long id) {
        return em.find(UsuarioMODEL.class, id);
    }
}
