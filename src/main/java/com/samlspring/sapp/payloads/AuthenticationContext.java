package com.samlspring.sapp.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.samlspring.sapp.entityImpl.LocalUser;

public class AuthenticationContext {

    @JsonProperty
    String TOKEN;
    @JsonProperty
    LocalUser localUser;

    public AuthenticationContext(String TOKEN, LocalUser localUser) {
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
