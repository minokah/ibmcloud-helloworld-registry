package org.cs4471.helloworld_registry.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Component("registryService")
public class RegistryService {
    // Hash map for service listings
    LinkedHashMap<String, String> servers = new LinkedHashMap<>();

    public boolean addURLToService(String service, String url) {
        servers.putIfAbsent(service, url);
        return true;
    }

    public boolean removeURLFromService(String service, String url) {
        if (servers.containsKey(service)) {
            servers.remove(url);
            return true;
        }

        return false;
    }

    public String getURL(String s) {
        return servers.get(s);
    }

    public void flush() {
        servers.clear();
    }
}
