package com.onlycoders.backendalugo.model.entity.produto.templates;

import java.util.Arrays;
import java.util.List;

public class ProdutoAluguel {

    String Id_usuario;

    String Id_produto;

    String Nome;

    String Descricao_curta;

    String Descricao;

    Double Valor_base_diaria;

    Double Valor_base_mensal;

    Double Valor_produto;

    String Data_compra;

    int Qtd_alugueis;

    Double Total_ganhos;

    Double Media_avaliacao;

    byte[] Capa_foto;

    List<DtAlugadas> dt_alugadas;

    List<Categorias> categorias;

    Boolean Ativo;

    Boolean publicado;

    public String getId_usuario() {
        return Id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        Id_usuario = id_usuario;
    }

    public String getId_produto() {
        return Id_produto;
    }

    public void setId_produto(String id_produto) {
        Id_produto = id_produto;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getDescricao_curta() {
        return Descricao_curta;
    }

    public void setDescricao_curta(String descricao_curta) {
        Descricao_curta = descricao_curta;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public Double getValor_base_diaria() {
        return Valor_base_diaria;
    }

    public void setValor_base_diaria(Double valor_base_diaria) {
        Valor_base_diaria = valor_base_diaria;
    }

    public Double getValor_base_mensal() {
        return Valor_base_mensal;
    }

    public void setValor_base_mensal(Double valor_base_mensal) {
        Valor_base_mensal = valor_base_mensal;
    }

    public Double getValor_produto() {
        return Valor_produto;
    }

    public void setValor_produto(Double valor_produto) {
        Valor_produto = valor_produto;
    }

    public String getData_compra() {
        return Data_compra;
    }

    public void setData_compra(String data_compra) {
        Data_compra = data_compra;
    }

    public int getQtd_alugueis() {
        return Qtd_alugueis;
    }

    public void setQtd_alugueis(int qtd_alugueis) {
        Qtd_alugueis = qtd_alugueis;
    }

    public Double getTotal_ganhos() {
        return Total_ganhos;
    }

    public void setTotal_ganhos(Double total_ganhos) {
        Total_ganhos = total_ganhos;
    }

    public Double getMedia_avaliacao() {
        return Media_avaliacao;
    }

    public void setMedia_avaliacao(Double media_avaliacao) {
        Media_avaliacao = media_avaliacao;
    }

    public List<DtAlugadas> getDt_alugadas() {
        return dt_alugadas;
    }

    public void setDt_alugadas(List<DtAlugadas> dt_alugadas) {
        this.dt_alugadas = dt_alugadas;
    }

    public List<Categorias> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categorias> categorias) {
        this.categorias = categorias;
    }

    public byte[] getCapa_foto() {
        return Capa_foto;
    }

    public void setCapa_foto(byte[] capa_foto) {
        this.Capa_foto = capa_foto;
    }

    public Boolean getAtivo() {
        return Ativo;
    }

    public void setAtivo(Boolean ativo) {
        Ativo = ativo;
    }

    public Boolean getPublicado() {
        return publicado;
    }

    public void setPublicado(Boolean publicado) {
        this.publicado = publicado;
    }
}
