package org.cs4471.helloworld_registry.controller;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.cs4471.helloworld_registry.Response;
import org.cs4471.helloworld_registry.service.RegistryRegistrar;
import org.cs4471.helloworld_registry.service.ServiceEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistryController {
    @Autowired
    private RegistryRegistrar registryRegistrar;

    @GetMapping("/flush")
    public String Flush() {
        registryRegistrar.flush();
        System.out.println("Registry : Flushed all services");
        return "Flushed all services";
    }

    @GetMapping("/register")
    public Response Register(HttpServletRequest request) {
        String name = request.getParameter("name");
        String url = request.getParameter("url");
        String desc = request.getParameter("desc");

        if (registryRegistrar.addService(new ServiceEntry(name, url, desc))) {
            System.out.println(String.format("Registry : Service Registered : %s : %s : %s", name, url, desc));
            return new Response(200, "Registry", String.format("Successfully registered %s service", name));
        }

        return new Response(404, "Registry", String.format("Failed to register %s service", name));
    }

    @GetMapping("/deregister")
    public Response Deregister(HttpServletRequest request) {
        String name = request.getParameter("name");

        if (registryRegistrar.removeService(name)) {
            System.out.println(String.format("Registry : Service Deregistered : %s", name));
            return new Response(200, "Registry", String.format("Successfully deregistered %s service", name));
        }

        return new Response(404, "Registry", String.format("%s Service was not found in registry", name));
    }
}
