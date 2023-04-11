package org.example.service;

import org.example.exception.FileCanNotBeParsedException;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataProcessedService {
    public List<String> readTheFile(String fileName) {
        Path path = Paths.get(fileName);
        List<String> parsedData = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                parsedData.add(reader.readLine());
            }
        } catch (IOException e) {
            new FileCanNotBeParsedException("There is a problem with parsing the file: " + fileName);
        }
        return parsedData;
    }
}
