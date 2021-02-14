package com.onlycoders.backendalugo.model.entity.pagamento;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class WebHookPagamento {

    @Id
    Integer id;

    String action;

    String api_version;

    Integer application_id;

    String date_created;

    Boolean live_mode;

    String type;

    Integer user_id;

    Integer version;

    @OneToOne
    com.onlycoders.backendalugo.model.entity.pagamento.Data data;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getApplication_id() {
        return application_id;
    }

    public void setApplication_id(Integer application_id) {
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

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public com.onlycoders.backendalugo.model.entity.pagamento.Data getData() {
        return data;
    }

    public void setData(com.onlycoders.backendalugo.model.entity.pagamento.Data data) {
        this.data = data;
    }
}
