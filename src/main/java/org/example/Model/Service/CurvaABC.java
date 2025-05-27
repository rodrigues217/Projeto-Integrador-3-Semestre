/*package org.example.Controller;

import jakarta.persistence.EntityManager;
import org.example.Model.Entity.ProdutosMODEL;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CurvaABC {
    public static List<ProdutosMODEL> classificar(List<ProdutosMODEL> produtos) {
        // 1. Calcular o valor de consumo de cada produto (preço * quantidade vendida)
        for (ProdutosMODEL p : produtos) {
            double valorConsumo = p.getValor() * p.getQuantidade_vendida();
            p.setValorConsumo(valorConsumo);
        }

        // 2. Criar uma cópia mutável e ordenar
        List<ProdutosMODEL> listaMutavel = new ArrayList<>(produtos);
        listaMutavel.sort(Comparator.comparingDouble(ProdutosMODEL::getValorConsumo).reversed());

        // 3. Calcular o total do consumo
        double total = listaMutavel.stream().mapToDouble(ProdutosMODEL::getValorConsumo).sum();

        // 4. Se não houve consumo, todos são categoria C
        if (total == 0) {
            for (ProdutosMODEL p : listaMutavel) {
                p.setCategoria("C");
            }
            return listaMutavel;
        }

        // 5. Calcular o percentual acumulado e classificar
        double acumulado = 0.0;

        for (ProdutosMODEL p : listaMutavel) {
            double percentualAcumulado = (acumulado / total) * 100;

            if (percentualAcumulado <= 70) {
                p.setCategoria("A");
            } else if (percentualAcumulado <= 90) {
                p.setCategoria("B");
            } else {
                p.setCategoria("C");
            }

            acumulado += p.getValorConsumo();
        }

        return listaMutavel;
    }

    public void aplicarCurvaABC() {
        List<ProdutosMODEL> produtos = em.createQuery("SELECT p FROM Produtos p", ProdutosMODEL.class).getResultList();
        CurvaABC.classificar(produtos);
    }

    public void aplicarCurvaABCEmLista(List<ProdutosMODEL> produtos) {
        CurvaABC.classificar(produtos);
    }

    public void aplicarCurvaABCEmProduto(ProdutosMODEL produto) {
        if (produto != null) {
            CurvaABC.classificar(List.of(produto));
        }
    }


}*/