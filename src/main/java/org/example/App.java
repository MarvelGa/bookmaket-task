package org.example;

import org.example.exception.FileProperlyReadException;
import org.example.service.DataProcessedService;

import java.io.IOException;

public class App {
    private static DataProcessedService service = new DataProcessedService();

    public static void main(String[] args) {
        String fileName = "input.txt";
        try {
            service.readAndWriteResult(fileName);
        } catch (IOException ex) {
            ex.getMessage();
        } catch (FileProperlyReadException ex) {
            ex.getMessage();
        }
    }

}
