package com.onlycoders.backendalugo.model.entity.logs;

import com.fasterxml.jackson.annotation.JsonGetter;

public interface RetornoErrosBackendController {

    @JsonGetter("controller")
    String getController();

    @JsonGetter("quantidade")
    int getQuantidade();

    @JsonGetter("mes")
    String getMes();
}
