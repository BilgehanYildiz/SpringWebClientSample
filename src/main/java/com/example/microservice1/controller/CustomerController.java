package com.example.microservice1.controller;


import com.example.microservice1.model.Email;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.microservice1.model.Customer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping(path = "api/customer/author")
    public ResponseEntity getAuthor() throws Exception
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

    @PostMapping(path = "api/customer",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCustomer(@RequestBody Customer request) throws Exception
    {
        //TODO Customer Creation process
        //

        Email email=new Email();
        email.setName(request.getName());
        email.setEmail(request.getEmail());


        ObjectMapper objectMapper = new ObjectMapper();
        String rmqmessage= objectMapper.writeValueAsString(email);

        rabbitTemplate.convertAndSend("EVENTMESSAGE.EXCHANGE","DEMO.QUEUE",rmqmessage);




        return ResponseEntity.ok("Musteri yaratildi");
    }


}
