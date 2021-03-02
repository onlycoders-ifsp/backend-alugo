package com.onlycoders.backendalugo.model.entity.aluguel.template;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornaAvaliacoes {

    @JsonAlias("nome_avaliador")
    String getNome_avaliador();

    @JsonAlias("comentario")
    String getComentario();

    @JsonAlias("nota")
    String getNota();
}
