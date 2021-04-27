package com.samlspring.sapp.webclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.samlspring.sapp.entityImpl.LocalUser;

public class TokenPayload {

    @JsonProperty
    String TOKEN;
    @JsonProperty
    LocalUser localUser;

    public TokenPayload(String TOKEN, LocalUser localUser) {
        this.TOKEN = TOKEN;
        this.localUser = localUser;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public LocalUser getLocalUser() {
        return localUser;
    }
}
