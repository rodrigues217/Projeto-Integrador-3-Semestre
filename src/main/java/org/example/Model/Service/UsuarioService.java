package org.example.Model.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.PerfilUsuario;
import org.example.Model.Entity.UsuarioMODEL;
import org.example.Model.Repository.FuncionarioRepository;
import org.example.Model.Repository.UsuarioRepository;
import org.example.Model.Util.HibernateUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final FuncionarioRepository funcionarioRepository = new FuncionarioRepository();
    // Scanner não é mais necessário para a lógica de login Swing
    // private final Scanner scanner = new Scanner(System.in);

    // Método de autenticação para ser usado pela interface Swing
    public UsuarioMODEL autenticarViaSwing(String login, String senha) {
        EntityManager em = HibernateUtil.getEntityManager();
        UsuarioMODEL usuarioLogado = null;
        try {
            usuarioLogado = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha", UsuarioMODEL.class)
                    .setParameter("login", login)
                    .setParameter("senha", senha)
                    .getSingleResult();

            // Se autenticado, atualiza o último login
            if (usuarioLogado != null) {
                em.getTransaction().begin();
                usuarioLogado.setUltimoLogin(LocalDateTime.now());
                em.merge(usuarioLogado);
                em.getTransaction().commit();
            }
        } catch (NoResultException e) {
            // Usuário ou senha inválidos, retorna null
            return null;
        } catch (PersistenceException e) {
            // Outro erro de persistência (ex: problema de conexão)
            e.printStackTrace(); // Logar o erro
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null; // Retorna null em caso de erro
        } catch (Exception e) {
            // Outros erros inesperados
            e.printStackTrace(); // Logar o erro
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return usuarioLogado;
    }


    // --- Métodos existentes (mantidos, mas podem precisar de refatoração para remover Scanner se forem usados por Swing) ---

    // Este método fazerLogin original usa Scanner e não é ideal para Swing
    public UsuarioMODEL fazerLogin(Scanner scanner) {
        EntityManager em = HibernateUtil.getEntityManager();
        UsuarioMODEL usuarioLogado = null;

        try {
            while (usuarioLogado == null) {
                System.out.println("*** LOGIN ***");
                System.out.print("Login: ");
                String login = scanner.next();
                System.out.print("Senha: ");
                String senha = scanner.next();

                // Reutiliza a lógica de autenticação e atualização de último login
                usuarioLogado = autenticarViaSwing(login, senha);

                if (usuarioLogado == null) {
                    System.out.println("Credenciais inválidas. Tente novamente.");
                } else {
                    System.out.println("Último login: " +
                            (usuarioLogado.getUltimoLogin() != null ? usuarioLogado.getUltimoLogin() : "Nunca"));
                    System.out.println("Login bem-sucedido!\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Rollback não é necessário aqui pois autenticarViaSwing já trata
        } finally {
            // EntityManager é fechado dentro de autenticarViaSwing
        }

        return usuarioLogado;
    }

    // Método privado original, agora a lógica está em autenticarViaSwing
    // private UsuarioMODEL autenticarUsuario(EntityManager em, String login, String senha) { ... }

    // Métodos CRUD (manter ou adaptar para Swing se necessário)
    public void criarUsuario(String login, String senha, String perfilStr) {
        // Adaptação para não usar Scanner diretamente
        List<UsuarioMODEL> usuarios = usuarioRepository.listarTodos();
        for (UsuarioMODEL u : usuarios) {
            if (u.getLogin().equalsIgnoreCase(login)) {
                System.out.println("Já existe um usuário com esse login.");
                // Em Swing, isso seria um JOptionPane.showMessageDialog
                return;
            }
        }

        try {
            UsuarioMODEL usuario = new UsuarioMODEL(login, senha, Enum.valueOf(PerfilUsuario.class, perfilStr.toUpperCase()));
            usuarioRepository.salvar(usuario);
            System.out.println("Usuário cadastrado com sucesso!");
            // Em Swing, JOptionPane.showMessageDialog
        } catch (IllegalArgumentException e) {
            System.out.println("Perfil inválido. Use 'ADM', 'GERENTE' ou 'OPERADOR'.");
            // Em Swing, JOptionPane.showMessageDialog
        }
    }

    public List<UsuarioMODEL> listarUsuarios() {
        return usuarioRepository.listarTodos();
        // A exibição seria feita na interface Swing
    }

    public UsuarioMODEL buscarUsuarioPorId(Long id) {
        return usuarioRepository.buscarPorId(id);
    }

    public void atualizarUsuario(Long id, String novoLogin, String novaSenha, String perfilStr) {
        // Adaptação para não usar Scanner
        UsuarioMODEL usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            // Em Swing, JOptionPane.showMessageDialog
            return;
        }

        List<UsuarioMODEL> usuarios = usuarioRepository.listarTodos();
        boolean loginDuplicado = usuarios.stream()
                .anyMatch(u -> !u.getId().equals(id) && u.getLogin().equalsIgnoreCase(novoLogin));

        if (loginDuplicado) {
            System.out.println("Já existe outro usuário com esse login.");
            // Em Swing, JOptionPane.showMessageDialog
            return;
        }

        usuario.setLogin(novoLogin);
        usuario.setSenha(novaSenha);

        try {
            usuario.setPerfil(Enum.valueOf(PerfilUsuario.class, perfilStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
            System.out.println("Perfil inválido. Atualização cancelada.");
            // Em Swing, JOptionPane.showMessageDialog
            return;
        }

        usuarioRepository.atualizar(usuario);
        System.out.println("Usuário atualizado com sucesso!");
        // Em Swing, JOptionPane.showMessageDialog
    }

    public boolean deletarUsuario(Long id) {
        // Adaptação para não usar Scanner e retornar status
        UsuarioMODEL usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            // Em Swing, JOptionPane.showMessageDialog
            return false;
        }

        // Verifica se está vinculado a funcionário (usando o repositório diretamente)
        List<FuncionarioMODEL> funcionariosVinculados = funcionarioRepository.buscarPorUsuario(usuario);
        if (!funcionariosVinculados.isEmpty()) {
            System.out.println("Este usuário está vinculado a um ou mais funcionários.");
            System.out.println("Não é possível deletar um usuário vinculado.");
            // Em Swing, JOptionPane.showMessageDialog
            return false;
        }

        // A confirmação (s/n) deve ser feita na interface Swing antes de chamar este método
        usuarioRepository.deletar(id);
        System.out.println("Usuário deletado com sucesso!");
        // Em Swing, JOptionPane.showMessageDialog
        return true;
    }
}

