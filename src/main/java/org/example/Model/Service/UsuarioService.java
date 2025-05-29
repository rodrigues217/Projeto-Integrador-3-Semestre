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
import java.util.Scanner;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final FuncionarioRepository funcionarioRepository = new FuncionarioRepository();
    private final Scanner scanner = new Scanner(System.in);

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

                usuarioLogado = autenticarUsuario(em, login, senha);

                if (usuarioLogado == null) {
                    System.out.println("Credenciais inválidas. Tente novamente.");
                } else {
                    System.out.println("Último login: " +
                            (usuarioLogado.getUltimoLogin() != null ? usuarioLogado.getUltimoLogin() : "Nunca"));

                    em.getTransaction().begin();
                    usuarioLogado.setUltimoLogin(LocalDateTime.now());
                    em.merge(usuarioLogado);
                    em.getTransaction().commit();

                    System.out.println("Login bem-sucedido!\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }

        return usuarioLogado;
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
    public void criarUsuario() {
        System.out.print("Login do usuário: ");
        String login = scanner.nextLine();

        List<UsuarioMODEL> usuarios = usuarioRepository.listarTodos();
        for (UsuarioMODEL u : usuarios) {
            if (u.getLogin().equalsIgnoreCase(login)) {
                System.out.println("Já existe um usuário com esse login.");
                return;
            }
        }

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        System.out.print("Perfil (ADM, GERENTE ou OPERADOR ): ");

        String perfilStr = scanner.nextLine().toUpperCase();

        try {
            UsuarioMODEL usuario = new UsuarioMODEL(login, senha, Enum.valueOf(PerfilUsuario.class, perfilStr));
            usuarioRepository.salvar(usuario);
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Perfil inválido. Use 'ADM' , 'FUNCIONARIO' ou 'GERENTE' ");
        }
    }

    public void listarUsuarios() {
        List<UsuarioMODEL> usuarios = usuarioRepository.listarTodos();
        if (usuarios.isEmpty()) {
            System.out.println("Não há usuários cadastrados.");
            return;
        }

        for (UsuarioMODEL usuario : usuarios) {
            System.out.println(usuario);
            FuncionarioMODEL funcionario = funcionarioRepository.buscarPorId(usuario.getId());
            if (funcionario != null) {
                System.out.println("Funcionario: " + funcionario.getNome());
            }
        }
    }

    public void atualizarUsuario() {
        List<UsuarioMODEL> usuarios = usuarioRepository.listarTodos();
        if (usuarios.isEmpty()) {
            System.out.println("Não há usuários cadastrados.");
            return;
        }

        listarUsuarios();
        System.out.print("Digite o ID do usuário que deseja atualizar: ");
        Long id = Long.parseLong(scanner.nextLine());

        UsuarioMODEL usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        System.out.print("Novo login: ");
        String novoLogin = scanner.nextLine();

        boolean loginDuplicado = usuarios.stream()
                .anyMatch(u -> !u.getId().equals(id) && u.getLogin().equalsIgnoreCase(novoLogin));

        if (loginDuplicado) {
            System.out.println("Já existe outro usuário com esse login.");
            return;
        }

        usuario.setLogin(novoLogin);

        System.out.print("Nova senha: ");
        usuario.setSenha(scanner.nextLine());

        System.out.print("Novo perfil (ADM, GERENTE ou OPERADOR): ");
        String perfilStr = scanner.nextLine().toUpperCase();

        try {
            usuario.setPerfil(Enum.valueOf(PerfilUsuario.class, perfilStr));
        } catch (IllegalArgumentException e) {
            System.out.println("Perfil inválido. Atualização cancelada.");
            return;
        }

        usuarioRepository.atualizar(usuario);
        System.out.println("Usuário atualizado com sucesso!");
    }

    public void deletarUsuario() {
        List<UsuarioMODEL> usuarios = usuarioRepository.listarTodos();
        if (usuarios.isEmpty()) {
            System.out.println("Não há usuários cadastrados.");
            return;
        }

        listarUsuarios();
        System.out.print("Digite o ID do usuário que deseja deletar: ");
        Long id = Long.parseLong(scanner.nextLine());

        UsuarioMODEL usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        FuncionarioMODEL funcionario = funcionarioRepository.buscarPorId(id);
        if (funcionario != null) {
            System.out.println("Este usuário está vinculado ao funcionário: " + funcionario.getNome());
            System.out.println("Não é possível deletar um usuário vinculado a um funcionário.");
            return;
        }

        System.out.print("Tem certeza que deseja deletar este usuário? (s/n): ");
        String confirmacao = scanner.nextLine();
        if (!confirmacao.equalsIgnoreCase("s")) {
            System.out.println("Operação cancelada.");
            return;
        }

        usuarioRepository.deletar(id);
        System.out.println("Usuário deletado com sucesso!");
    }


}
