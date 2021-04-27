package com.samlspring.sapp.controllers;

import com.samlspring.sapp.SAMLcore.SAMLUserDetailsServiceImpl;
import com.samlspring.sapp.agents.TokenAgent;
import com.samlspring.sapp.entityImpl.LocalUser;
import com.samlspring.sapp.webclient.TokenPayload;
import com.samlspring.sapp.webclient.WebClientApi;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("auth/redirect-action")
public class TokenRedirectController {

    private final Logger LOG = Logger.getRootLogger();
    @Autowired
    private SAMLUserDetailsServiceImpl samlUserDetailsService;
    @Autowired
    private TokenAgent tokenAgent;
    @Autowired
    private WebClientApi webClientApi;

    private final String DUMMY_STORE_TOKEN_URI = "http://localhost:8090/dummy_store/get_token";
    private final String DUMMY_STORE_AUTH_URI = "http://localhost:8090/dummy_store/authenticate/";

    @RequestMapping(method = RequestMethod.GET, path = "/dummy_store")
    public void tokenRedirect(HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LocalUser localUser = samlUserDetailsService.loadUserBySAML((SAMLCredential) auth.getCredentials());
        byte[] TOKEN = tokenAgent.generateNewToken();

        String URL_TOKEN = new String(Base64.getUrlEncoder().encode(TOKEN));
        String POST_b64_TOKEN = new String(TOKEN);

        webClientApi.sendData(new TokenPayload(POST_b64_TOKEN, localUser)).flux().toStream().findFirst().get();

        String AUTH_REDIRECT = DUMMY_STORE_AUTH_URI + URL_TOKEN;
/**
 * send user token
 * redirect:{URI}/{TOKEN_VALUE}
 */
        try {
            response.sendRedirect(AUTH_REDIRECT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
