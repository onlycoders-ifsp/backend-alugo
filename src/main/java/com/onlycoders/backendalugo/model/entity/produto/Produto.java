package com.onlycoders.backendalugo.model.entity.produto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Produto {

    @JsonAlias({"id_usuario"})
    public String idUsuario;

    @Id
    @JsonAlias({"id_produto"})
    public String idProduto;

    @JsonAlias({"nome"})
    public String nome;

    @JsonAlias({"descricao_curta"})
    public String descricaoCurta;

    @JsonAlias({"descricao"})
    public String descricao;

    @JsonAlias({"valor_base_diaria"})
    public Double valorBaseDiaria;

    @JsonAlias({"valor_base_mensal"})
    public Double valorBaseMensal;

    @JsonAlias({"valor_produto"})
    public Double valorProduto;

    @JsonAlias({"data_compra"})
    public String dataCompra;

    @JsonAlias({"qtd_alugueis"})
    public int qtdAlugueis;

    @JsonAlias({"total_ganhos"})
    public Double totalGanhos;

    @JsonAlias({"media_avaliacao"})
    public Double mediaAvaliacao;

    @JsonAlias({"capa_foto"})
    public String capaFoto;

    @JsonAlias({"ativo"})
    public Boolean ativo;
}
