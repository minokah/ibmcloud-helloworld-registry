package org.cs4471.helloworld_registry.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.cs4471.helloworld_registry.shared.RegistryStatus;
import org.cs4471.helloworld_registry.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistryController {
    @Autowired
    private RegistryService registryService;

    @GetMapping("/flush")
    public String Flush() {
        registryService.flush();
        return "Success";
    }

    @GetMapping("/register")
    public String Register(HttpServletRequest request) {
        String url = request.getRemoteHost();

        if (registryService.addURLToService("HelloWorld", url)) {
            System.out.println("Service Registered : HelloWorld : " + url);
            return RegistryStatus.REGISTRY_STATUS.SUCCESS.toString();
        }

        return RegistryStatus.REGISTRY_STATUS.FAILURE.toString();
    }

    @GetMapping("/deregister")
    public String Deregister(HttpServletRequest request) {
        String url = request.getRemoteHost();
        if (registryService.removeURLFromService("HelloWorld", url)) {
            System.out.println("Service Deregistered : HelloWorld : " + url);
            return "Removed";
        }
        return String.format("Could not remove %s from %s!", "HelloWorld", url);
    }
}
