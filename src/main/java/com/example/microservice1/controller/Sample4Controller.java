package com.example.microservice1.controller;

import com.example.microservice1.model.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;


import java.time.Duration;

@RestController
@RequestMapping("api/sample4")
public class Sample4Controller {



    @GetMapping(path = "entity")
    public ResponseEntity getEntity()
    {

        WebClient client = WebClient.create("http://localhost:8092/api/author");

        ResponseEntity<Email> statusEntity  = client.get().retrieve().toEntity(Email.class).block();
        return ResponseEntity.ok("Bilgiler alındı");
    }

    @GetMapping(path = "timeout")
    public ResponseEntity getTimeout()
    {

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(4));
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://localhost:8092/api/author").build();

                ResponseEntity<Email> statusEntity  = client.get().retrieve().toEntity(Email.class).block();
        return ResponseEntity.ok("Bilgiler alındı");
    }

}
