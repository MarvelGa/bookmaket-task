package org.example.service;

import org.example.exception.FileCanNotBeParsedException;
import org.example.exception.FileProperlyReadException;
import org.example.model.BookMarket;
import org.example.model.Detail;
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
    private BookMarket bookMarket;

    public void readAndWriteResult(String fileName){
        bookMarket = new BookMarket();
        List<String> lines = readTheFile(fileName);
        proceedData(lines);
    }

    private List<String> readTheFile(String fileName) {
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

      private void proceedData(List<String> line){
        for (String elem: line){
            String [] parsedLine = elem.split(",");
            if (parsedLine[0].equals("u")){
               updateOrderBook(parsedLine[1], parsedLine[2], parsedLine[3]);
            }
        }

      }
      private void updateOrderBook(String price, String size, String type){
          Detail detail = new Detail(Integer.valueOf(price), Integer.valueOf(size));
        if (type.equals("ask")){
            bookMarket.setAsk(List.of(detail));
        }else if(type.equals("bit")){
            bookMarket.setBid(List.of(detail));
        }else{
            throw new FileProperlyReadException("Can't read file properly");
        }
      }

}
