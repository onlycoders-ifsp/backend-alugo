package com.onlycoders.backendalugo.model.entity.logs;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlycoders.backendalugo.model.entity.Meses;
import java.util.List;

public class ErrosControllerMetodos {

    @JsonAlias("controller")
    String controller;

    @JsonAlias("quantidade")
    int quantidade;

    @JsonAlias("mes")
    List<Meses> mes;

    @JsonProperty("metodos")
    @JsonAlias("metodo")
    List<ErrosBackendMetodo> errosBackendMetodos;

    public ErrosControllerMetodos(String controller, int quantidade, List<Meses> mes, List<ErrosBackendMetodo> errosBackendMetodos) {
        this.controller = controller;
        this.quantidade = quantidade;
        this.mes = mes;
        this.errosBackendMetodos = errosBackendMetodos;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
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

    public List<ErrosBackendMetodo> getErrosBackendMetodos() {
        return errosBackendMetodos;
    }

    public void setErrosBackendMetodos(List<ErrosBackendMetodo> errosBackendMetodos) {
        this.errosBackendMetodos = errosBackendMetodos;
    }
}
