package com.onlycoders.backendalugo.model.entity.aluguel.template;

import com.fasterxml.jackson.annotation.JsonGetter;

public interface RetornaAluguelEncontro {

    @JsonGetter("id_aluguel")
    String getId_aluguel();

    @JsonGetter("cep_entrega")
    String getCep_entrega();

    @JsonGetter("logradouro_entrega")
    String getLogradouro_entrega();

    @JsonGetter("bairro_entrega")
    String getBairro_entrega();

    @JsonGetter("descricao_entrega")
    String getDescricao_entrega();

    @JsonGetter("data_entrega")
    String getData_entrega();

    @JsonGetter("cep_devolucao")
    String getCep_devolucao();

    @JsonGetter("logradouro_devolucao")
    String getLogradouro_devolucao();

    @JsonGetter("bairro_devolucao")
    String getBairro_devolucao();

    @JsonGetter("descricao_devolucao")
    String getDescricao_devolucao();

    @JsonGetter("data_devolucao")
    String getData_devolucao();

    @JsonGetter("aceite_locador")
    String getAceite_locador();

    @JsonGetter("observacao_recusa")
    String getObservacao_recusa();
}
