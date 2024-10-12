package org.cs4471.helloworld_registry.controller;

import org.cs4471.helloworld_registry.service.RegistryService;
import org.cs4471.helloworld_registry.shared.RegistryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class HelloWorldController
{
    @Autowired
    private RegistryService registryService;

    @GetMapping("/helloworld")
    public String helloWorld() {
        String result = WebClient.builder().baseUrl(registryService.getURL("HelloWorld")).build().get().uri("/register").retrieve().bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(Exception.class, ex -> Mono.just("Failed to retrieve from HelloWorld!"))
                .block();

        return result;
    }
}
