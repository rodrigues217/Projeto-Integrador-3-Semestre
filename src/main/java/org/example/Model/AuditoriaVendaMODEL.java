package org.example.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "AuditoriaVenda") // Nome usado nas queries
@Table(name = "auditoria_vendas")
public class AuditoriaVendaMODEL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private ProdutosMODEL produto;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private UsuarioMODEL vendedor;

    private Integer quantidade;

    @Column(name = "data_venda")
    private LocalDateTime dataVenda;

    @PrePersist
    protected void onCreate() {
        this.dataVenda = LocalDateTime.now();
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public ProdutosMODEL getProduto() {
        return produto;
    }

    public void setProduto(ProdutosMODEL produto) {
        this.produto = produto;
    }

    public UsuarioMODEL getVendedor() {
        return vendedor;
    }

    public void setVendedor(UsuarioMODEL vendedor) {
        this.vendedor = vendedor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }
}
