package com.onlycoders.backendalugo.model.entity.aluguel;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornaAluguel {

    @JsonAlias("id_aluguel")
    public String getId_aluguel();

    @JsonAlias("id_produto")
    public String getId_produto();

    @JsonAlias("id_locatario")
    public String getId_locatario();

    @JsonAlias("id_locador")
    public String getId_locador();

    @JsonAlias("data_inicio")
    public String getData_inicio();

    @JsonAlias("data_fim")
    public String getData_fim();

    @JsonAlias("valor_aluguel")
    public Double getValor_aluguel();

    @JsonAlias("data_saque")
    public String getData_saque();
}
