package com.onlycoders.backendalugo.model.entity.produto.templates;

import com.fasterxml.jackson.annotation.*;

public class RetornaProduto {

    @JsonAlias("id_produto")
    private String idProduto;
    private String nome;
    private String descricao;
    private Double valorBaseDiaria;
    private Double valorBaseMensal;
    private Double valorProduto;
    private String dataCompra;
    private Boolean ativo;

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValorBaseDiaria() {
        return valorBaseDiaria;
    }

    public void setValorBaseDiaria(Double valorBaseDiaria) {
        this.valorBaseDiaria = valorBaseDiaria;
    }

    public Double getValorBaseMensal() {
        return valorBaseMensal;
    }

    public void setValorBaseMensal(Double valorBaseMensal) {
        this.valorBaseMensal = valorBaseMensal;
    }

    public Double getValorProduto() {
        return valorProduto;
    }

    public void setValorProduto(Double valorProduto) {
        this.valorProduto = valorProduto;
    }

    public String getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(String dataCompra) {
        this.dataCompra = dataCompra;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
