package org.cs4471.helloworld_registry.service;

import lombok.Getter;

public class ServiceEntry {
    @Getter
    private String name, url, desc;

    public ServiceEntry(String name, String url, String desc) {
        this.name = name;
        this.url = url;
        this.desc = desc;
    }
}
