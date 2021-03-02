package com.onlycoders.backendalugo.model.entity.aluguel.template;

import java.util.List;

public class ExtratoLocador {
    Double saldo;

    List<ExtratoLocadorDetalhe> extrato;

    public ExtratoLocador(Double saldo,List<ExtratoLocadorDetalhe> extrato){
        this.saldo = saldo;
        this.extrato = extrato;
    }
}
