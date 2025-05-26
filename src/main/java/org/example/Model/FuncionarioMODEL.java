package org.example.Model;

import jakarta.persistence.*;

@Entity(name = "Funcionario")
@Table(name = "funcionarios")
public class FuncionarioMODEL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String endereco;
    private String documento;

    @Column(name = "totalvendas")
    private double totalVendas;

    @ManyToOne
    @JoinColumn(name = "setor_id")
    private SetorMODEL setor;

    @OneToOne(optional = false)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", unique = true)
    private UsuarioMODEL usuario;

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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
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
