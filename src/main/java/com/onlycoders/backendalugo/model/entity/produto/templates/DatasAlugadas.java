package com.onlycoders.backendalugo.model.entity.produto.templates;

import com.fasterxml.jackson.annotation.JsonGetter;

public interface DatasAlugadas {
    @JsonGetter("dt_inicio")
    String getDt_inicio();

    @JsonGetter("dt_fim")
    String getDt_Fim();
}
