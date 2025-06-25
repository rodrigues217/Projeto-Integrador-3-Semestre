package org.example.Model.Entity;

import jakarta.persistence.*;

@Entity(name = "Comprador")
@Table(name = "compradores")
public class CompradorMODEL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nome;
    private String telefone;
    private String CPF;

    private String email;      // <== ADICIONAR
    private String endereco;   // <== ADICIONAR

    public CompradorMODEL() {}

    public CompradorMODEL(String nome, String telefone, String CPF) {
        this.nome = nome;
        this.telefone = telefone;
        this.CPF = CPF;
    }


    // Construtor completo
    public CompradorMODEL(String nome, String telefone, String CPF, String email, String endereco) {
        this.nome = nome;
        this.telefone = telefone;
        this.CPF = CPF;
        this.email = email;
        this.endereco = endereco;
    }

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCPF() { return CPF; }
    public void setCPF(String CPF) { this.CPF = CPF; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}
