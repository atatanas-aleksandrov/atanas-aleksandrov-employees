package com.employees.employees.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class FileStorageConfig {

    private final String location = "/Users/aleksandrovv/Desktop/csv";

    public String getLocation() {
        return this.location;
    }
}
