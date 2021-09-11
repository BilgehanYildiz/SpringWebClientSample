package com.example.microservice1.controller;

import com.example.microservice1.model.Email;
import io.netty.handler.timeout.ReadTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@RestController
@RequestMapping("api/sample5")
public class Sample5RetryController {

    @GetMapping(path = "retry")
    public ResponseEntity retrySample()
    {

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(4));
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://localhost:8092/api/author").build();

        Email email  = client.get().retrieve().bodyToMono(Email.class).retry(3).block();
       // Email email2  = client.get().retrieve().bodyToMono(Email.class).retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(5))).block();
        return ResponseEntity.ok("Bilgiler alındı");
    }

    @GetMapping(path = "retryforspecificerror")
    public ResponseEntity retryForSpecific()
    {

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(4));
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://localhost:8092/api/author").build();

        Email email  = client.get().retrieve()

                /*.onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new Exception("Server error unexpected")))*/
                .bodyToMono(Email.class).
                        retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)).
                filter(throwable -> throwable instanceof WebClientRequestException)).block();
        // Email email2  = client.get().retrieve().bodyToMono(Email.class).retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(5))).block();
        return ResponseEntity.ok("Bilgiler alındı");
    }

}
