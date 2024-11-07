package org.cs4471.registry.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;

// Do a sweeping check for all the services to see if they are alive
@Component("registryHeartbeat")
public class RegistryHeartbeat extends Thread {
    @Autowired
    private RegistryRegistrar registryRegistrar;

    private int sleepTimer = 60000; // 1 minute

    public void run() {
        System.out.println("Registry : Heartbeat started");

        while (true) {
            // Sleep for a while
            try {
                Thread.sleep(sleepTimer);
            }
            catch (Exception e) {
                System.out.println("Registry : Heartbeat : Exception: " + e);
            }

            System.out.println(String.format("Registry : Heartbeat : Checking for %s services", registryRegistrar.getServicesList().size()));

            // Do checks and remove from registry if no response in 1 minute
            ArrayList<ServiceEntry> inactive = new ArrayList<>();
            ArrayList<ServiceEntry> services = registryRegistrar.getServicesList();
            for (ServiceEntry e : services) {
                String response = WebClient.builder().baseUrl(e.getUrl()).build().get().uri("/heartbeat").retrieve().bodyToMono(String.class)
                        .timeout(Duration.ofMinutes(1))
                        .onErrorResume(Exception.class, ex -> Mono.just(""))
                        .block();

                if (response == null || response.isEmpty()) {
                    inactive.add(e);
                    System.out.println(String.format("Registry : Heartbeat : %s is inactive, will be removed", e.getName()));
                }
            }

            // Remove inactives
            if (!inactive.isEmpty()) System.out.println(String.format("Registry : Removed %d inactive services", inactive.size()));
            for (ServiceEntry e : inactive) registryRegistrar.removeService(e);
        }
    }
}
