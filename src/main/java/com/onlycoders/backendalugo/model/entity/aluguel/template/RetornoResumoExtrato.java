package com.onlycoders.backendalugo.model.entity.aluguel.template;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornoResumoExtrato {

    @JsonAlias("Valor_Sacado")
    Double getValor_sacado();

    @JsonAlias("Valor_A_Receber")
    Double getValor_a_receber();

    @JsonAlias("Total")
    Double getTotal();
}
