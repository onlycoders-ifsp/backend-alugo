package com.onlycoders.backendalugo.model.entity;


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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String nome;
    @ManyToOne()
    private Usuario locatario;
    @Column
    private Double valorBaseDiaria;
    @Column
    private Double valorBaseMensal;
    @Column
    private Double valorProduto;
    @Column
    private Date dataCompra;
    @Column
    private Boolean ativo;

}
