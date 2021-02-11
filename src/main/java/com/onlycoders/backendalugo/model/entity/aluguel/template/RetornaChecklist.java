package com.onlycoders.backendalugo.model.entity.aluguel.template;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornaChecklist {

    @JsonAlias("id_aluguel")
    String getId_aluguel();

    @JsonAlias("descricao")
    String getDescricao();

    @JsonAlias("foto")
    String getFoto();

    @JsonAlias("ok_locador")
    String getOk_locador();

    @JsonAlias("motivo_recusa")
    String getMotivoRecusa();
}
