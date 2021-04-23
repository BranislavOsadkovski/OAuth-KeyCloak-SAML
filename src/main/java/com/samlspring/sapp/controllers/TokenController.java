package com.samlspring.sapp.controllers;

import com.fasterxml.jackson.annotation.JsonValue;
import com.samlspring.sapp.SAMLcore.SAMLUserDetailsServiceImpl;
import com.samlspring.sapp.agents.TokenAgent;
import com.samlspring.sapp.entityImpl.LocalUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.*;

@Controller
@RequestMapping("dummy_store")
public class TokenController {
    private final Logger LOG = Logger.getRootLogger();
    @Autowired
    private SAMLUserDetailsServiceImpl samlUserDetailsService;
    @Autowired
    private TokenAgent tokenAgent;

    private final String dummyStoreURI = "http://localhost:8090/dummy_store/get_token";


    @RequestMapping(method = RequestMethod.GET, path = "/redirect")
    public String tokenRedirect(HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LocalUser localUser = samlUserDetailsService.loadUserBySAML((SAMLCredential) auth.getCredentials());

        LOG.debug(this.getClass().getSimpleName() + " LOCAL User : " + localUser.toString());


        //Service generates a unique Token
        String TOKEN = tokenAgent.generateNewUrlToken();

        /**
         * send user token
         * redirect:{URI}/{TOKEN_VALUE}
         */
        String dummyStoreURI = "http://localhost:8090/dummy_store/authenticate/" + TOKEN;

        return "redirect:" + dummyStoreURI;
    }




    private class Body implements Serializable {
        @JsonValue
        String token;
        @JsonValue
        String username;

        public Body() {
        }

        public Body(String token, String username) {
            this.token = token;
            this.username = username;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "token='" + token + '\'' +
                    ", username='" + username + '\'' +
                    '}';
        }
    }
}
