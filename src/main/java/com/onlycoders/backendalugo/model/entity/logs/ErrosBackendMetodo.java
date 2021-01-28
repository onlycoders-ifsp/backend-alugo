package com.onlycoders.backendalugo.model.entity.logs;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.onlycoders.backendalugo.model.entity.Meses;
import java.util.List;

public class ErrosBackendMetodo {

    @JsonAlias("metodo")
    String metodo;

    @JsonAlias("endpoint")
    String endpoint;

    @JsonAlias("quantidade")
    int quantidade;

    @JsonAlias("mes")
    List<Meses> mes;

    public ErrosBackendMetodo(String metodo, String endpoint, int quantidade, List<Meses> mes) {
        this.metodo = metodo;
        this.endpoint = endpoint;
        this.quantidade = quantidade;
        this.mes = mes;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
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
