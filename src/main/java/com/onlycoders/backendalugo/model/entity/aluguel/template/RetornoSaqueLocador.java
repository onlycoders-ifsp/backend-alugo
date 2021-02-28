package com.onlycoders.backendalugo.model.entity.aluguel.template;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornoSaqueLocador {

    @JsonAlias("id_usuario")
    String getId_usuario();

    @JsonAlias("valor_debito")
    Double getValor_debito();

    @JsonAlias("id_produto")
    String getId_produto();

    @JsonAlias("nome_produto")
    String getNome_produto();

    @JsonAlias("descricao_curta")
    String getDescricao_curta();

    @JsonAlias("id_pagamento")
    String getId_pagamento();

    @JsonAlias("id_saque")
    String getId_saque();
}
