package com.onlycoders.backendalugo.model.entity.aluguel.template;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
public class AluguelDetalhe {

    public AluguelDetalhe(String idProduto, String nomeProduto, String idLocatario, String nomeLocatario, Byte[] capaFoto, String dataInicio, String dataFim, Double valorAluguel, Double valorGanho, String dataDevolucao) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.idLocatario = idLocatario;
        this.nomeLocatario = nomeLocatario;
        this.capaFoto = capaFoto;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorAluguel = valorAluguel;
    }

    @JsonAlias("id_produto")
    public String idProduto;

    @JsonAlias("nome_produto")
    public String nomeProduto;

    @JsonAlias("id_locatario")
    public String idLocatario;

    @JsonAlias("nome_locatario")
    public String nomeLocatario;

    @JsonAlias("capa_foto")
    public Byte[] capaFoto;

    @JsonAlias("data_inicio")
    public String dataInicio;

    @JsonAlias("data_fim")
    public String dataFim;

    @JsonAlias("valor_aluguel")
    public Double valorAluguel;

    @JsonAlias("valor_ganho")
    public Double valorGanho;

    @JsonAlias("data_devolucao")
    public String dataDevolucao;

    @JsonAlias("qtd_alugueis")
    private int qtdAlugueis;

    @JsonAlias("total_ganho")
    private Double totalGanho;

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getIdLocatario() {
        return idLocatario;
    }

    public void setIdLocatario(String idLocatario) {
        this.idLocatario = idLocatario;
    }

    public String getNomeLocatario() {
        return nomeLocatario;
    }

    public void setNomeLocatario(String nomeLocatario) {
        this.nomeLocatario = nomeLocatario;
    }

    public Byte[] getCapaFoto() {
        return capaFoto;
    }

    public void setCapaFoto(Byte[] capaFoto) {
        this.capaFoto = capaFoto;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public Double getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(Double valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public Double getValorGanho() {
        return valorGanho;
    }

    public void setValorGanho(Double valorGanho) {
        this.valorGanho = valorGanho;
    }

    public String getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(String dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

}
