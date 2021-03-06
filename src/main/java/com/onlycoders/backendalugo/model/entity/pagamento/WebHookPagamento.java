package com.onlycoders.backendalugo.model.entity.pagamento;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class WebHookPagamento {

    @Id
    String id;

    String action;

    String api_version;

    String application_id;

    String date_created;

    Boolean live_mode;

    String type;

    String user_id;

    @OneToOne
    com.onlycoders.backendalugo.model.entity.pagamento.Data data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getApi_version() {
        return api_version;
    }

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }

    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public Boolean getLive_mode() {
        return live_mode;
    }

    public void setLive_mode(Boolean live_mode) {
        this.live_mode = live_mode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public com.onlycoders.backendalugo.model.entity.pagamento.Data getData() {
        return data;
    }

    public void setData(com.onlycoders.backendalugo.model.entity.pagamento.Data data) {
        this.data = data;
    }
}
