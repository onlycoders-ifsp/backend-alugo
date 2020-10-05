package com.onlycoders.backendalugo.model.entity.produto.templates;

import com.fasterxml.jackson.annotation.*;

public class RetornaProduto {

    private String idProduto;
    private String nome;
    private String descricaoCurta;
    private String descricao;
    private Double valorBaseDiaria;
    private Double valorBaseMensal;
    private Double valorProduto;
    private String dataCompra;
    private int qtdAlugueis;
    private Double totalGanhos;
    private Double mediaAvaliacao;
    private String capaFoto;
    private Boolean ativo;

    @JsonGetter("id_produto")
    public String getIdProduto() {
        return idProduto;
    }

    @JsonSetter("id_produto")
    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    @JsonGetter("nome")
    public String getNome() {
        return nome;
    }

    @JsonSetter("nome")
    public void setNome(String nome) {
        this.nome = nome;
    }

    @JsonGetter("descricao")
    public String getDescricao() {
        return descricao;
    }

    @JsonSetter("descricao")
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @JsonGetter("valor_base_diario")
    public Double getValorBaseDiaria() {
        return valorBaseDiaria;
    }

    @JsonSetter("valor_base_diario")
    public void setValorBaseDiaria(Double valorBaseDiaria) {
        this.valorBaseDiaria = valorBaseDiaria;
    }

    @JsonGetter("valor_base_mensal")
    public Double getValorBaseMensal() {
        return valorBaseMensal;
    }

    @JsonSetter("valor_base_mensal")
    public void setValorBaseMensal(Double valorBaseMensal) {
        this.valorBaseMensal = valorBaseMensal;
    }

    @JsonGetter("valor_produto")
    public Double getValorProduto() {
        return valorProduto;
    }

    @JsonSetter("valor_produto")
    public void setValorProduto(Double valorProduto) {
        this.valorProduto = valorProduto;
    }

    @JsonGetter("data_compra")
    public String getDataCompra() {
        return dataCompra;
    }

    @JsonSetter("data_compra")
    public void setDataCompra(String dataCompra) {
        this.dataCompra = dataCompra;
    }

    @JsonGetter("ativo")
    public Boolean getAtivo() {
        return ativo;
    }

    @JsonSetter("ativo")
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @JsonGetter("descricao_curta")
    public String getDescricaoCurta() {
        return descricaoCurta;
    }

    @JsonSetter("descricao_curta")
    public void setDescricaoCurta(String descricao_curta) {
        this.descricaoCurta = descricao_curta;
    }

    @JsonGetter("qtd_alugueis")
    public int getQtdAlugueis() {
        return qtdAlugueis;
    }

    @JsonSetter("qtd_alugueis")
    public void setQtdAlugueis(int qtdAlugueis) {
        this.qtdAlugueis = qtdAlugueis;
    }

    @JsonGetter("total_ganhos")
    public Double getTotalGanhos() {
        return totalGanhos;
    }

    @JsonSetter("total_ganhos")
    public void setTotalGanhos(Double totalGanhos) {
        this.totalGanhos = totalGanhos;
    }

    @JsonGetter("media_avaliacao")
    public Double getMediaAvaliacao() {
        return mediaAvaliacao;
    }

    @JsonSetter("media_avaliacao")
    public void setMediaAvaliacao(Double mediaAvaliacao) {
        this.mediaAvaliacao = mediaAvaliacao;
    }

    @JsonGetter("capa_foto")
    public String getCapaFoto() {
        return capaFoto;
    }

    @JsonSetter("capa_foto")
    public void setCapaFoto(String capaFoto) {
        this.capaFoto = capaFoto;
    }
}
