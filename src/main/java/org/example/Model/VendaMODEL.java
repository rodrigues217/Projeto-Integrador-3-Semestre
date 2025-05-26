package org.example.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Venda") // nome da entidade usado nas queries HQL/JPQL
@Table(name = "vendas")
public class VendaMODEL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "funcionarios_id", nullable = false)
    private FuncionarioMODEL funcionario;

    @ManyToOne
    @JoinColumn(name = "comprador_id", nullable = false)
    private CompradorMODEL comprador;

    @Column(name = "data_venda")
    private LocalDateTime dataVenda;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FuncionarioMODEL getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioMODEL funcionario) {
        this.funcionario = funcionario;
    }

    public CompradorMODEL getComprador() {
        return comprador;
    }

    public void setComprador(CompradorMODEL comprador) {
        this.comprador = comprador;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }
}
