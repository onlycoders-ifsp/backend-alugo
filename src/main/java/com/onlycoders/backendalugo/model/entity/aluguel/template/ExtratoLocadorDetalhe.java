package com.onlycoders.backendalugo.model.entity.aluguel.template;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface ExtratoLocadorDetalhe {

    @JsonAlias("valor")
    Double getValor();

    @JsonAlias("descricao")
    String getDescricao();

    @JsonAlias("data_inclusao")
    String getData_inclusao();
}
