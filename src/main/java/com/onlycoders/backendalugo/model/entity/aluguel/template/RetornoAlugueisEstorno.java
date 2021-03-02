package com.onlycoders.backendalugo.model.entity.aluguel.template;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornoAlugueisEstorno {

    @JsonAlias("id_aluguel")
    String getId_aluguel();

    @JsonAlias("id_pagamento_mp")
    String getId_pagamento_mp();

    @JsonAlias("valor")
    Double getValor();

    @JsonAlias("retencao")
    Double getRetencao();

    @JsonAlias("status")
    Integer getStatus();
}
