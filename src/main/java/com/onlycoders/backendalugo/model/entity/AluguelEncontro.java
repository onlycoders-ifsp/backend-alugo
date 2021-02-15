package com.onlycoders.backendalugo.model.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AluguelEncontro {

    @JsonAlias("id_aluguel")
    @Id
    private String id_aluguel;

    @JsonAlias("cep_entrega")
    private String cep_entrega;

    @JsonAlias("logradouro_entrega")
    private String logradouro_entrega;

    @JsonAlias("bairro_entrega")
    private String bairro_entrega;

    @JsonAlias("descricao_entrega")
    private String descricao_entrega;

    @JsonAlias("data_entrega")
    private String data_entrega;

    @JsonAlias("cep_devolucao")
    private String cep_devolucao;

    @JsonAlias("logradouro_devolucao")
    private String logradouro_devolucao;

    @JsonAlias("bairro_devolucao")
    private String bairro_devolucao;

    @JsonAlias("descricao_devolucao")
    private String descricao_devolucao;

    @JsonAlias("data_devolucao")
    private String data_devolucao;

    @JsonAlias("aceite_locador")
    private boolean aceite_locador;

    @JsonAlias("observacao_recusa")
    private String observacao_recusa;

    public String getId_aluguel() {
        return id_aluguel;
    }

    public void setId_aluguel(String id_aluguel) {
        this.id_aluguel = id_aluguel;
    }

    public String getCep_entrega() {
        return cep_entrega;
    }

    public void setCep_entrega(String cep_entrega) {
        this.cep_entrega = cep_entrega;
    }

    public String getLogradouro_entrega() {
        return logradouro_entrega;
    }

    public void setLogradouro_entrega(String logradouro_entrega) {
        this.logradouro_entrega = logradouro_entrega;
    }

    public String getBairro_entrega() {
        return bairro_entrega;
    }

    public void setBairro_entrega(String bairro_entrega) {
        this.bairro_entrega = bairro_entrega;
    }

    public String getDescricao_entrega() {
        return descricao_entrega;
    }

    public void setDescricao_entrega(String descricao_entrega) {
        this.descricao_entrega = descricao_entrega;
    }

    public String getData_entrega() {
        return data_entrega;
    }

    public void setData_entrega(String data_entrega) {
        this.data_entrega = data_entrega;
    }

    public String getCep_devolucao() {
        return cep_devolucao;
    }

    public void setCep_devolucao(String cep_devolucao) {
        this.cep_devolucao = cep_devolucao;
    }

    public String getLogradouro_devolucao() {
        return logradouro_devolucao;
    }

    public void setLogradouro_devolucao(String logradouro_devolucao) {
        this.logradouro_devolucao = logradouro_devolucao;
    }

    public String getBairro_devolucao() {
        return bairro_devolucao;
    }

    public void setBairro_devolucao(String bairro_devolucao) {
        this.bairro_devolucao = bairro_devolucao;
    }

    public String getDescricao_devolucao() {
        return descricao_devolucao;
    }

    public void setDescricao_devolucao(String descricao_devolucao) {
        this.descricao_devolucao = descricao_devolucao;
    }

    public String getData_devolucao() {
        return data_devolucao;
    }

    public void setData_devolucao(String data_devolucao) {
        this.data_devolucao = data_devolucao;
    }

    public boolean isAceite_locador() {
        return aceite_locador;
    }

    public void setAceite_locador(boolean aceite_locador) {
        this.aceite_locador = aceite_locador;
    }

    public String getObservacao_recusa() {
        return observacao_recusa;
    }

    public void setObservacao_recusa(String observacao_recusa) {
        this.observacao_recusa = observacao_recusa;
    }
}
