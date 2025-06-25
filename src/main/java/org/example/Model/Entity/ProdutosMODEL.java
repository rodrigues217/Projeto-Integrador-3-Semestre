package org.example.Model.Entity;

import jakarta.persistence.*;

@Entity(name = "Produtos")
@Table(name = "produtos")
public class ProdutosMODEL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "estoque", nullable = false)
    private Integer estoque;

    @Column(name = "curva_abc", length = 1)
    private String curvaAbc;

    @Column(name = "cod_prod", length = 25)
    private String codProd;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaProdutoMODEL categoria;


    @Override
    public String toString() {
        return nome; // ou qualquer campo que represente o nome do produto
    }

    public ProdutosMODEL() {
    }

    public ProdutosMODEL(String nome, Double valor, Integer estoque,String codProd, CategoriaProdutoMODEL categoria) {
        this.nome = nome;
        this.valor = valor;
        this.estoque = estoque;
        this.codProd = codProd;
        this.categoria = categoria;
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

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public String getCurvaAbc() {
        return curvaAbc;
    }

    public void setCurvaAbc(String curvaAbc) {
        this.curvaAbc = curvaAbc;
    }

    public String getCodProd() {
        return codProd;
    }

    public void setCodProd(String codProd) {
        this.codProd = codProd;
    }

    public CategoriaProdutoMODEL getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProdutoMODEL categoria) {
        this.categoria = categoria;
    }


}
