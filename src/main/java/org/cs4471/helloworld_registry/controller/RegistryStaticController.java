package org.cs4471.helloworld_registry.controller;

import org.cs4471.helloworld_registry.service.RegistryRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistryStaticController {
    @Autowired
    private RegistryRegistrar registryRegistrar;

    @GetMapping("/")
    public String Root(Model model) {
        model.addAttribute("HelloWorldURL", registryRegistrar.getURL("HelloWorld"));
        return "listing";
    }
}
