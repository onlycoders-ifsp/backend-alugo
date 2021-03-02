package com.onlycoders.backendalugo.model.entity.logs;

import com.fasterxml.jackson.annotation.JsonGetter;

public interface RetornoErrosBackendMetodos {

    @JsonGetter("metodo")
    String getMetodo();

    @JsonGetter("endpoint")
    String getEndpoint();

    @JsonGetter("quantidade")
    int getQuantidade();

    @JsonGetter("mes")
    String getMes();
}
