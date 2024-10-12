package org.cs4471.helloworld_registry.service;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component("registryRegistrar")
public class RegistryRegistrar {
    // Hash map for service listings
    LinkedHashMap<String, String> servers = new LinkedHashMap<>();

    public boolean addService(String service, String url) {
        servers.putIfAbsent(service, url);
        return true;
    }

    public boolean removeService(String service) {
        if (servers.containsKey(service)) {
            servers.remove(service);
            return true;
        }

        return false;
    }

    public String getURL(String s) {
        return servers.get(s);
    }

    public LinkedHashMap<String, String> getAllURLs() {
        return servers;
    }

    public void flush() {
        servers.clear();
    }
}
