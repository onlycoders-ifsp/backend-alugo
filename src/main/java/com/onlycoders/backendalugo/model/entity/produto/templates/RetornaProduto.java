package com.onlycoders.backendalugo.model.entity.produto.templates;

import com.google.gson.annotations.SerializedName;

public interface RetornaProduto {

    @SerializedName("Nome_Usuario")
    String getNomeUsuario();

    @SerializedName("Nome_Produto")
    String getNomeProduto();

    @SerializedName("Decricao")
    String getDescricao();

    @SerializedName("BaseDiaria")
    Double getValorBaseDiaria();

    @SerializedName("BaseMensal")
    Double getValorBaseMensal();

    @SerializedName("ValorProduto")
    Double getValorProduto();

    @SerializedName("DataCompra")
    String getDataCompra();

    @SerializedName("DataInclusao")
    String getDataInclusao();
}
