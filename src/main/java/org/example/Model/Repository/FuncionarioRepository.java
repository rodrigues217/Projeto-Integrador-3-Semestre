package org.example.Model.Repository;

import jakarta.persistence.EntityManager;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Util.HibernateUtil;

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
        List<FuncionarioMODEL> funcionarios = em.createQuery("FROM FuncionarioMODEL", FuncionarioMODEL.class).getResultList();
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
        em.getTransaction().begin();
        FuncionarioMODEL funcionario = em.find(FuncionarioMODEL.class, id);
        if (funcionario != null) {
            em.remove(funcionario);
        }
        em.getTransaction().commit();
        em.close();
    }
}
