package org.example.exception;

public class FileCanNotBeParsedException extends RuntimeException{
    public FileCanNotBeParsedException() {
    }

    public FileCanNotBeParsedException(String message) {
        super(message);
    }
}
