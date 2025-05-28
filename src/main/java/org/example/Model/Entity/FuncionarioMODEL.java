package org.example.Model.Entity;

import jakarta.persistence.*;

@Entity(name = "Funcionario")
@Table(name = "funcionarios")
public class FuncionarioMODEL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "documento", length = 255)
    private String documento;

    @Column(name = "endereco", length = 255)
    private String endereco;

    @Column(name = "telefone", nullable = false, length = 255)
    private String telefone;

    @Column(name = "totalvendas")
    private double totalVendas;

    @ManyToOne
    @JoinColumn(name = "setor_id")
    private SetorMODEL setor;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioMODEL usuario;

    public FuncionarioMODEL() {
    }

    public FuncionarioMODEL(String nome, String documento, String endereco, String telefone, SetorMODEL setor, UsuarioMODEL usuario) {
        this.nome = nome;
        this.documento = documento;
        this.endereco = endereco;
        this.telefone = telefone;
        this.setor = setor;
        this.usuario = usuario;
        this.totalVendas = 0.0;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public double getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(double totalVendas) {
        this.totalVendas = totalVendas;
    }

    public SetorMODEL getSetor() {
        return setor;
    }

    public void setSetor(SetorMODEL setor) {
        this.setor = setor;
    }

    public UsuarioMODEL getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioMODEL usuario) {
        this.usuario = usuario;
    }

}
