package org.example.Model.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "Usuario")
@Table(name = "usuarios")
public class UsuarioMODEL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", length = 255, nullable = false, unique = true)
    private String login;

    @Column(name = "senha", length = 255, nullable = false)
    private String senha;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", length = 20, nullable = false)
    private PerfilUsuario perfil;


    public UsuarioMODEL() {
    }


    public UsuarioMODEL(String login, String senha, PerfilUsuario perfil) {
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }


    public boolean isAdmin() {
        return PerfilUsuario.ADM.equals(this.perfil);
    }

    @Override
    public String toString() {
        return "UsuarioMODEL{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", perfil=" + perfil +
                '}';
    }
}