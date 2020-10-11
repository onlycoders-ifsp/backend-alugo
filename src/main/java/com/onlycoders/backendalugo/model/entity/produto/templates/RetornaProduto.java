package com.onlycoders.backendalugo.model.entity.produto.templates;

import com.fasterxml.jackson.annotation.*;

public interface RetornaProduto {

    @JsonSetter("id_usuario")
    String getId_usuario();

    @JsonGetter("id_produto")
    String getId_produto();

    @JsonGetter("nome")
    String getNome();

    @JsonGetter("descricao_curta")
    String getDescricao_curta();

    @JsonGetter("descricao")
    String getDescricao();

    @JsonGetter("valor_base_diario")
    Double getValor_base_diario();

    @JsonGetter("valor_base_mensal")
    Double getValor_base_mensal();

    @JsonGetter("valor_produto")
    Double getValor_produto();

    @JsonGetter("data_compra")
    String getData_compra();

    @JsonGetter("qtd_alugueis")
    int getQtd_alugueis();

    @JsonGetter("total_ganhos")
    Double getTotal_ganhos();

    @JsonGetter("media_avaliacao")
    Double getMedia_avaliacao();

    @JsonGetter("capa_foto")
    String getCapa_foto();

    @JsonGetter("ativo")
    Boolean getAtivo();
}
