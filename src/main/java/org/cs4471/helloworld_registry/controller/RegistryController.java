package org.cs4471.helloworld_registry.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.cs4471.helloworld_registry.RegistryStatus;
import org.cs4471.helloworld_registry.service.RegistryRegistrar;
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
        return "Flushed all services";
    }

    @GetMapping("/register")
    public String Register(HttpServletRequest request) {
        String url = request.getServerName();
        String service = request.getParameter("service");

        if (registryRegistrar.addService(service, url)) {
            System.out.println(String.format("Registry : Service Registered : %s : %s", service, url));
            return RegistryStatus.REGISTRY_STATUS.SUCCESS.toString();
        }

        return RegistryStatus.REGISTRY_STATUS.FAILURE.toString();
    }

    @GetMapping("/deregister")
    public String Deregister(HttpServletRequest request) {
        String url = request.getServerName();
        String service = request.getParameter("service");

        if (registryRegistrar.removeService(service)) {
            String msg = String.format("Registry : Service Deregistered : %s : %s", service, url);
            System.out.println(msg);
            return msg;
        }
        return String.format("Could not remove %s from %s!", service, url);
    }
}
