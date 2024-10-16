package org.cs4471.helloworld_registry.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Component("registryRegistrar")
public class RegistryRegistrar {
    // Hash map for service listings
    LinkedHashMap<String, ServiceEntry> services = new LinkedHashMap<>();

    public boolean addService(ServiceEntry entry) {
        services.putIfAbsent(entry.getName(), entry);
        return true;
    }

    public boolean removeService(String entry) {
        if (services.containsKey(entry)) {
            services.remove(entry);
            return true;
        }

        return false;
    }

    public boolean removeService(ServiceEntry entry) {
        return removeService(entry.getName());
    }

    public ServiceEntry getService(String s) {
        return services.get(s);
    }

    public LinkedHashMap<String, ServiceEntry> getServicesMap() {
        return services;
    }

    public ArrayList<ServiceEntry> getServicesList() {
        ArrayList<ServiceEntry> list = new ArrayList<>();
        for (ServiceEntry e : services.values()) list.add(e);
        return list;
    }

    public void flush() {
        services.clear();
    }
}
