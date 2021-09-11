package com.example.microservice1.controller;

import com.example.microservice1.model.Customer;
import com.example.microservice1.model.Email;
import com.example.microservice1.model.MyResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("api/sample2")
public class Sample2Controller {

    @PostMapping(path = "api/customer",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCustomer(@RequestBody Customer request) throws Exception
    {
        //TODO Customer Creation process
        //

        Email email=new Email();
        email.setName(request.getName());
        email.setEmail(request.getEmail());

        WebClient client = WebClient.create("http://localhost:8092");
        MyResponse sendEmail = client.post()
                .uri("/api/sendemail")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(email), Email.class)
                .retrieve()
                .bodyToMono(MyResponse.class).block();


        return ResponseEntity.ok("Musteri yaratildi");
    }

    @PostMapping(path = "api/customerbulk",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCustomerBulk(@RequestBody Customer request) throws Exception
    {
        //TODO Customer Creation process
        //

        Email email=new Email();
        email.setName(request.getName());
        email.setEmail(request.getEmail());

        WebClient client = WebClient.create("http://localhost:8092");
        List<Email> sendEmail = client.post()
                .uri("/api/sendbulk")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(email), Email.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Email>>() {}).block();

        return ResponseEntity.ok("Musteri yaratildi");
    }

}
