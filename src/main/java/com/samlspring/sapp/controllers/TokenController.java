package com.samlspring.sapp.controllers;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samlspring.sapp.SAMLcore.SAMLUserDetailsServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("dummy_store")
public class TokenController {

    @Autowired
    private SAMLUserDetailsServiceImpl samlUserDetailsService;

    private static final Logger LOG = Logger.getRootLogger();

    private final String dummyStoreURI = "http://localhost:8090/dummy_store/get_token";


    @RequestMapping(method = RequestMethod.GET, path = "/redirect")
    public String tokenRedirect(HttpServletRequest request) {

        //Invalidate session locally for this SP
        Optional<Cookie> c = Arrays.stream(request.getCookies()).filter((cookie) -> cookie.getName().equals("JSESSIONID")).findFirst();
        c.ifPresent((cookie) -> cookie.setMaxAge(0));
        LOG.info(this.getClass().getSimpleName() + " Invalidating server SESSION ");

        //Service generates a unique Token
        String TOKEN_VALUE = UUID.randomUUID().toString();

        // send store token
/**
 *     TRY CREATE SAMLCredentials @Bean and make it cross functional as a service to load local SAML user into the application
 */
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        String httpToken = "TOKEN="+TOKEN_VALUE;
//        HttpEntity<String> httpEntity =
//                  new HttpEntity<String>(httpToken, headers);
//
//        restTemplate.postForObject(dummyStoreURI, httpEntity, String.class);
        /**
         * send user token
         * redirect:{URI}/{TOKEN_VALUE}
         */
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBasicAuth("username","password");


        Body body = new Body(TOKEN_VALUE,samlUserDetailsService.loadUser().getUsername());
//        body.token = TOKEN_VALUE;
//        body.username = samlUserDetailsService.loadUser().getUsername();

        HttpEntity<String> httpEntity =
                null;
        try {
            httpEntity = new HttpEntity<Body>(objectMapper.writeValueAsString(body), headers);
        } catch (JsonProcessingException e) {
            LOG.error(e);
        }

        restTemplate.postForEntity(dummyStoreURI, httpEntity, String.class);


        String dummyStoreURI = "http://localhost:8090/dummy_store/authenticate/" + TOKEN_VALUE;


        return "redirect:" + dummyStoreURI;
    }


    class Body implements Serializable {
        @JsonValue
        String token;
        @JsonValue
        String username;

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
    }
}
