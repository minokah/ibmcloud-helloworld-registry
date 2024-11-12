package org.cs4471.registry.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.cs4471.registry.Response;
import org.cs4471.registry.service.RegistryRegistrar;
import org.cs4471.registry.service.ServiceEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class RegistryController {
    @Autowired
    private RegistryRegistrar registrar;

    @GetMapping("/flush")
    public Response Flush(HttpServletRequest request) {
        String key = request.getParameter("key");
        
        if ("my_flush_key".equals(key)) {
            registrar.flush();
            System.out.println("Registry : Flushed all services");
            return new Response(200, "Registry", "All services flushed!");
        }

        return new Response(400, "Registry", "Invalid key entered to flush services");
    }

    @GetMapping("/register")
    public Response Register(HttpServletRequest request) {
        String name = request.getParameter("name");
        String url = request.getParameter("url");
        String desc = request.getParameter("desc");

        if (name == null) name = "";
        if (url == null) url = "";
        if (desc == null) desc = "";

        ServiceEntry entry = registrar.getService(name);

        // Service already exists, don't do anything
        if (entry != null) {
            return new Response(200, "Registry", String.format("Service %s is already registered on URL %s", entry.getName(), entry.getUrl()));
        }

        // Don't add if any fields are blank
        if (name.isEmpty() || url.isEmpty() || desc.isEmpty()) {
            return new Response(400, "Registry", "Fields cannot be blank when registering a service");
        }

        // Add new service
        if (registrar.addService(new ServiceEntry(name, url, desc))) {
            System.out.println(String.format("Registry : Service Registered : %s : %s : %s", name, url, desc));
            return new Response(200, "Registry", String.format("Successfully registered %s service", name));
        }

        return new Response(404, "Registry", String.format("Failed to register %s service", name));
    }

    @GetMapping("/deregister")
    public Response Deregister(HttpServletRequest request) {
        String name = request.getParameter("name");

        if (name == null) name = "";

        if (registrar.removeService(name)) {
            System.out.println(String.format("Registry : Service Deregistered : %s", name));
            return new Response(200, "Registry", String.format("Successfully deregistered %s service", name));
        }

        return new Response(404, "Registry", String.format("%s Service was not found in registry", name));
    }

    @GetMapping("/getServices")
    public ArrayList<ServiceEntry> getServices(HttpServletRequest request) {
        String nameFilter = request.getParameter("name");
        String descFitler = request.getParameter("desc");

        ArrayList<ServiceEntry> entries = registrar.getServicesList();
        ArrayList<ServiceEntry> toRemove = new ArrayList<>();

        boolean keep = true;
        for (ServiceEntry s : entries) {
            if (nameFilter != null && !nameFilter.isEmpty()) {
                if (!s.getName().toLowerCase().contains(nameFilter.toLowerCase())) keep = false;
            }

            if (descFitler != null && !descFitler.isEmpty()) {
                if (!s.getDesc().toLowerCase().contains(descFitler.toLowerCase())) keep = false;
            }

            if (!keep) toRemove.add(s);
        }

        entries.removeAll(toRemove);

        return entries;
    }
}
