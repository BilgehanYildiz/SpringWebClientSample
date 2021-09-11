package com.example.microservice1.controller;

import com.example.microservice1.model.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("api/sample1")
public class Sample1Controller {


    @GetMapping(path = "author/sync")
    public ResponseEntity getAuthor() throws Exception
    {
        WebClient client = WebClient.create("http://localhost:8092/api/author");
        WebClient.ResponseSpec responseSpec  = client.get().retrieve();
        Email responseBody = responseSpec.bodyToMono(Email.class).block();
        return ResponseEntity.ok("Bilgiler alındı");
    }


    @GetMapping(path = "author/async")
    public ResponseEntity getAuthorAsync() throws Exception
    {
        WebClient client = WebClient.create("http://localhost:8092/api/author");
        WebClient.ResponseSpec responseSpec  = client.get().retrieve();
        //Email responseBody = responseSpec.bodyToMono(Email.class).block();
        Mono<Email> emailMono=responseSpec.bodyToMono(Email.class);
        emailMono.subscribe(result->{System.out.println(result.getName() + OffsetDateTime.now());});
        Thread.sleep(20000);
        System.out.println("Çıkmadan önceki son adım"+  OffsetDateTime.now());
        return ResponseEntity.ok("Bilgiler alındı");
    }


}
