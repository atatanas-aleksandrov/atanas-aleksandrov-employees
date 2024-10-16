package com.employees.employees;

import com.employees.employees.config.FileStorageConfig;
import com.employees.employees.repository.file.FileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageConfig.class)
public class EmployeesApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeesApplication.class, args);
    }


    @Bean
    CommandLineRunner init(FileRepository storageService) {
        return args -> {
//            storageService.deleteAll();
            storageService.init();
        };
    }
}
