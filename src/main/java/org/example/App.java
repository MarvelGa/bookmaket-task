package org.example;

import lombok.RequiredArgsConstructor;
import org.example.service.DataProcessedService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class App implements CommandLineRunner {
   private final DataProcessedService service;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(App.class);
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        String fileName = "src/main/resources/input.txt";
        try {
            service.readTheFile(fileName);
        } catch (Exception ex) {

        }
    }
}
