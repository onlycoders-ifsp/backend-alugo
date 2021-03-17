package com.onlycoders.backendalugo.model.entity.aluguel.template;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornaProblemaPagamento {

    @JsonAlias("perfil")
    String getPerfil();

    @JsonAlias("id_aluguel")
    String getId_aluguel();

    @JsonAlias("id_usuario")
    String getId_usuario();

    @JsonAlias("id_pagamento")
    String getId_pagamento();

    @JsonAlias("tipo_problema")
    Integer getTipo_problema();

    @JsonAlias("descricao")
    String getDescricao();

    @JsonAlias("Valor_problema")
    Double getValor_problema();

    @JsonAlias("Valor_aluguel")
    Double getValor_aluguel();
}
