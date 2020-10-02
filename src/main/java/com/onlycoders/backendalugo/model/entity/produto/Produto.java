package com.onlycoders.backendalugo.model.entity.produto;


import com.onlycoders.backendalugo.model.entity.usuario.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Produto {

    @Id
    private String idProduto;

    @Column
    private String idUsuario;

    @Column
    private String nome;

    @Column
    private String descricao;

    @Column
    private Double valorBaseDiaria;

    @Column
    private Double valorBaseMensal;

    @Column
    private Double valorProduto;

    @Column
    private String dataCompra;

    @Column
    private Boolean isDeletado;

    @Column
    private String dataInclusao;

    @Transient
    @Column
    private String dataModificacao;
}
