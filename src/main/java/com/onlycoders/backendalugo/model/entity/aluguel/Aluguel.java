package com.onlycoders.backendalugo.model.entity.aluguel;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String id_produto;
    private String data_inicio;
    private String data_fim;
    private Double valor_aluguel;
    private String url_pagamento;

    public String getUrl_pagamento() {
        return url_pagamento;
    }

    public void setUrl_pagamento(String url_pagamento) {
        this.url_pagamento = url_pagamento;
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
}
