package org.example.Model.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.UsuarioMODEL;
import org.example.Model.Util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepository {

    public void salvar(FuncionarioMODEL funcionario) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(funcionario);
        em.getTransaction().commit();
        em.close();
    }

    public FuncionarioMODEL buscarPorId(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        FuncionarioMODEL funcionario = em.find(FuncionarioMODEL.class, id);
        em.close();
        return funcionario;
    }

    public List<FuncionarioMODEL> listarTodos() {
        EntityManager em = HibernateUtil.getEntityManager();
        List<FuncionarioMODEL> funcionarios = em.createQuery("FROM Funcionario", FuncionarioMODEL.class).getResultList();
        em.close();
        return funcionarios;
    }

    public void atualizar(FuncionarioMODEL funcionario) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(funcionario);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            FuncionarioMODEL funcionario = em.find(FuncionarioMODEL.class, id);
            if (funcionario != null) {
                em.remove(funcionario);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao deletar funcionário: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<FuncionarioMODEL> buscarPorUsuario(UsuarioMODEL usuario) {
        List<FuncionarioMODEL> funcionarios = listarTodos();
        List<FuncionarioMODEL> vinculados = new ArrayList<>();

        for (FuncionarioMODEL funcionario : funcionarios) {
            if (funcionario.getUsuario() != null && funcionario.getUsuario().getId().equals(usuario.getId())) {
                vinculados.add(funcionario);
            }
        }

        return vinculados;
    }

    public FuncionarioMODEL buscarFuncionarioPorCPF(String CPF) {
        EntityManager em = HibernateUtil.getEntityManager();
        FuncionarioMODEL funcionario = null;
        try {
            funcionario = em.createQuery("FROM Funcionario f WHERE f.CPF = :CPF", FuncionarioMODEL.class)
                    .setParameter("CPF", CPF)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Retorna null se não encontrar
        } finally {
            em.close();
        }
        return funcionario;
    }

}
