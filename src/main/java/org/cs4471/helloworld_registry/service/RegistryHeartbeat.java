package org.cs4471.helloworld_registry.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;

// Do a sweeping check for all the services to see if they are alive
@Component("registryHeartbeat")
public class RegistryHeartbeat extends Thread {
    @Autowired
    private RegistryRegistrar registryRegistrar;

    private int sleepTimer = 300000; // 5 minutes

    public void run() {
        System.out.println("Registry : Heartbeat started");

        while (true) {
            // Sleep for a while
            try {
                Thread.sleep(sleepTimer);
            }
            catch (Exception e) {
                System.out.println("Exception! " + e);
            }

            System.out.println(String.format("Registry : Heartbeat : Checking for %s services", registryRegistrar.getAllURLs().size()));

            // Do checks and remove from registry if no response in 1 minute
            ArrayList<String> inactive = new ArrayList<>();
            LinkedHashMap<String, String> servers = registryRegistrar.getAllURLs();
            for (String s : servers.keySet()) {
                Boolean response = WebClient.builder().baseUrl(s).build().get().uri("/heartbeat").retrieve().bodyToMono(Boolean.class)
                        .timeout(Duration.ofMinutes(1))
                        .onErrorResume(Exception.class, ex -> Mono.just(Boolean.FALSE))
                        .block();

                if (response == null || response.equals(Boolean.FALSE)) {
                    inactive.add(s);
                    System.out.println(String.format("Registry : Heartbeat : %s is inactive, will be removed", s));
                }
            }

            // Remove inactives
            for (String s : inactive) servers.remove(s);
        }
    }
}
