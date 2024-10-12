package org.cs4471.helloworld_registry.controller;

import org.cs4471.helloworld_registry.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistryStaticController {
    @Autowired
    private RegistryService registryService;

    @GetMapping("/")
    public String Root(Model model) {
        model.addAttribute("HelloWorldURL", registryService.getURL("HelloWorld"));
        return "listing";
    }
}
