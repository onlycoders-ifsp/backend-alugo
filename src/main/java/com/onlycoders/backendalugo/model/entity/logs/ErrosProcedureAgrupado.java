package com.onlycoders.backendalugo.model.entity.logs;

import com.onlycoders.backendalugo.model.entity.aluguel.template.Meses;

import java.util.List;

public class ErrosProcedureAgrupado {

    String procedure;

    int quantidade;

    List<Meses> mes;

    public ErrosProcedureAgrupado(String procedure, int quantidade, List<Meses> mes) {
        this.procedure = procedure;
        this.quantidade = quantidade;
        this.mes = mes;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public List<Meses> getMes() {
        return mes;
    }

    public void setMes(List<Meses> mes) {
        this.mes = mes;
    }
}
