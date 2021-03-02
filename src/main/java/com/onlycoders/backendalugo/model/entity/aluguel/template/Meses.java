package com.onlycoders.backendalugo.model.entity.aluguel.template;

public class Meses {

    String mesDescricao;

    int mesQuantidade;

    public Meses(String descricaoMes, int qtdOcorrencias) {
        this.mesDescricao = descricaoMes;
        this.mesQuantidade = qtdOcorrencias;
    }

    public String getMesDescricao() {
        return mesDescricao;
    }

    public void setMesDescricao(String mesDescricao) {
        this.mesDescricao = mesDescricao;
    }

    public int getMesQuantidade() {
        return mesQuantidade;
    }

    public void setMesQuantidade(int mesQuantidade) {
        this.mesQuantidade = mesQuantidade;
    }
}
