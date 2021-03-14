package com.onlycoders.backendalugo.model.entity.aluguel.template;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface ExtratoLocadorDetalhe {

    @JsonAlias("id_pagamento")
    String getId_pagamento();

    @JsonAlias("valor")
    Double getValor();

    @JsonAlias("descricao")
    String getDescricao();

    @JsonAlias("sacado")
    Boolean getSacado();

    @JsonAlias("id_aluguel")
    String getId_aluguel();

    @JsonAlias("data_inclusao")
    String getData_inclusao();
}
