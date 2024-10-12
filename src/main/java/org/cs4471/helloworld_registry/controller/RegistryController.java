package org.cs4471.helloworld_registry.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.cs4471.helloworld_registry.RegistryStatus;
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
    public String Register(HttpServletRequest request) {
        String name = request.getParameter("name");
        String url = request.getParameter("url");
        String desc = request.getParameter("desc");

        if (registryRegistrar.addService(new ServiceEntry(name, url, desc))) {
            System.out.println(String.format("Registry : Service Registered : %s : %s : %s", name, url, desc));
            return RegistryStatus.REGISTRY_STATUS.SUCCESS.toString();
        }

        return RegistryStatus.REGISTRY_STATUS.FAILURE.toString();
    }

    @GetMapping("/deregister")
    public String Deregister(HttpServletRequest request) {
        String service = request.getParameter("service");

        if (registryRegistrar.removeService(service)) {
            String msg = String.format("Registry : Service Deregistered : %s", service);
            System.out.println(msg);
            return msg;
        }

        return String.format("Could not remove service %s!", service);
    }
}
