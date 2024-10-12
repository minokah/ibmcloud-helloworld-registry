package org.cs4471.helloworld_registry.controller;

import org.cs4471.helloworld_registry.service.RegistryRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class HelloWorldController
{
    private int timeoutSeconds = 30;

    @Autowired
    private RegistryRegistrar registryRegistrar;

    @GetMapping("/helloworld")
    public String helloWorld() {
        if (registryRegistrar.getURL("HelloWorld") == null) {
            String msg = "The HelloWorld service is not registered and cannot process /helloworld";
            System.out.println("Registry : " + msg);
            return msg;
        }

        String result = WebClient.builder().baseUrl(registryRegistrar.getURL("HelloWorld")).build().get().uri("/").retrieve().bodyToMono(String.class)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .onErrorResume(Exception.class, ex -> Mono.just(null))
                .block();

        if (result == null || result.isEmpty()) {
            System.out.println("Registry : Failed to retrieve from HelloWorld, removing service...");
            registryRegistrar.removeService("HelloWorld");
            return "Failed to retrieve from HelloWorld";
        }

        return result;
    }
}
