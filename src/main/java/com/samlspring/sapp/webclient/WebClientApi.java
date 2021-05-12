package com.samlspring.sapp.webclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientApi {

    @Autowired
    WebClient webClient;

    public Mono<ResponseEntity<Object>> sendPayloadData(String URI,TokenPayload tokenPayload) {

        return webClient.post()
                .uri(URI)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(tokenPayload)
                .retrieve()
                .toEntity(Object.class)
                ;
    }

}
