package org.example.Controller;


import org.example.Model.Entity.SetorMODEL;
import org.example.Model.Service.SetorService;

import java.util.List;

public class SetorController {

    private final SetorService setorService;

    public SetorController() {
        this.setorService = new SetorService();
    }


    public void criarSetor(String nome) throws Exception {
        setorService.criarSetor(nome);
    }


    public List<SetorMODEL> listarSetores() {
        return setorService.listarSetores();
    }


    public void atualizarSetor(Long id, String novoNome) throws Exception {
        setorService.atualizarSetor(id, novoNome);
    }

    public void deletarSetor(Long id) throws Exception {
        setorService.deletarSetor(id);
    }
}
