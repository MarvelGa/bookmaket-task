package org.example.exception;

public class FileProperlyReadException extends RuntimeException{

    public FileProperlyReadException(String message) {
        super(message);
    }
}
