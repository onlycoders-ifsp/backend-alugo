package com.onlycoders.backendalugo.model.entity.pagamento;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@lombok.Data
public class Data {

    @Id
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
