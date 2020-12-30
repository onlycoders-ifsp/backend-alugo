package com.onlycoders.backendalugo.model.entity.aluguel;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;

public interface RetornaAluguelDetalhe {

    @JsonGetter("id_produto")
    public String getId_produto();

    @JsonGetter("nome_produto")
    public String getNome_produto();

    @JsonGetter("foto_produto")
    byte[] getFoto_produto();

    @JsonGetter("id_locatario")
    public String getId_locatario();

    @JsonGetter("nome_locatario")
    public String getNome_locatario();

    @JsonGetter("foto_locatario")
    byte[] getFoto_locatario();

    @JsonGetter("data_inicio")
    public String getData_inicio();

    @JsonGetter("data_fim")
    public String getData_fim();

    @JsonGetter("valor_aluguel")
    public Double getValor_aluguel();

    @JsonGetter("valor_ganho")
    public Double getValor_ganho();

    @JsonGetter("data_devolucao")
    public String getData_devolucao();
}
