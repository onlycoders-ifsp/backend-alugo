package com.onlycoders.backendalugo.model.entity.aluguel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Aluguel {
    @Id
    private String id_aluguel;
    private String id_usuario = null;
    private String id_produto;
    private String data_inicio;
    private String data_fim;
    private Double valor_aluguel;
    private Double valor_debito = null;
    private String data_saque = null;

    public String getId_aluguel() {
        return id_aluguel;
    }

    public void setId_aluguel(String id_aluguel) {
        this.id_aluguel = id_aluguel;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getId_produto() {
        return id_produto;
    }

    public void setId_produto(String id_produto) {
        this.id_produto = id_produto;
    }

    public String getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    public String getData_fim() {
        return data_fim;
    }

    public void setData_fim(String data_fim) {
        this.data_fim = data_fim;
    }

    public Double getValor_aluguel() {
        return valor_aluguel;
    }

    public void setValor_aluguel(Double valor_aluguel) {
        this.valor_aluguel = valor_aluguel;
    }

    public Double getValor_debito() {
        return valor_debito;
    }

    public void setValor_debito(Double valor_debito) {
        this.valor_debito = valor_debito;
    }

    public String getData_saque() {
        return data_saque;
    }

    public void setData_saque(String data_saque) {
        this.data_saque = data_saque;
    }
}
