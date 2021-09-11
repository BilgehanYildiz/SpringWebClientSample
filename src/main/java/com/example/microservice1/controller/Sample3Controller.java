package com.example.microservice1.controller;

import com.example.microservice1.model.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/sample3")
public class Sample3Controller {

    @GetMapping(path = "author/exhange")
    public ResponseEntity getAuthor() throws Exception,Throwable
    {
        WebClient client = WebClient.create("http://localhost:8092/api/author");



        Mono<Object> monoresponse  = client.get().exchangeToMono(
                response->{
        if (response.statusCode().equals(HttpStatus.OK))
        {
            return response.bodyToMono(Email.class);
        } else if (response.statusCode().is4xxClientError()) {
            return Mono.error(new IllegalArgumentException("format hatası"));
        }
        else
            {
                return Mono.error(new Exception("beklenmeyen hata"));
                //Just En primitive mono dönüş şeklidir.Tek bir instance üretir
                //Defer her subscribe için farklı değer üretir.
            }
    });

        Email email= (Email) monoresponse.block();
        return ResponseEntity.ok("Bilgiler alındı");
    }
}
