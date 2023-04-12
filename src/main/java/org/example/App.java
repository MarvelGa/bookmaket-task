package org.example;

import lombok.RequiredArgsConstructor;
import org.example.exception.FileProperlyReadException;
import org.example.service.DataProcessedService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@RequiredArgsConstructor
@SpringBootApplication
public class App implements CommandLineRunner {
    private final DataProcessedService service;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(App.class);
        application.run(args);
    }

    @Override
    public void run(String... args) {
        String fileName = "src/main/resources/input.txt";
        try {
            service.readAndWriteResult(fileName);
        } catch (IOException ex) {
            ex.getMessage();
        } catch (FileProperlyReadException ex) {
            ex.getMessage();
        }
    }
}
