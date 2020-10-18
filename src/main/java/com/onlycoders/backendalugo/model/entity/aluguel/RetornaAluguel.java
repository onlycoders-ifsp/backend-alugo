package com.onlycoders.backendalugo.model.entity.aluguel;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornaAluguel {

    @JsonAlias("id_aluguel")
    public String getId_aluguel();

    @JsonAlias("id_produto")
    public String getId_produto();

    @JsonAlias("nome_produto")
    public String getNome_produto();

    @JsonAlias("id_locatario")
    public String getId_locatario();

    @JsonAlias("nome_locatario")
    public String getNome_locatario();

    @JsonAlias("locador")
    public String getNome_locador();

    @JsonAlias("data_inicio")
    public String getData_inicio();

    @JsonAlias("data_fim")
    public String getData_fim();

    @JsonAlias("valor_aluguel")
    public Double getValor_aluguel();

    @JsonAlias("valor_debito")
    public Double getValor_debito();

    @JsonAlias("data_saque")
    public String getData_saque();
}
