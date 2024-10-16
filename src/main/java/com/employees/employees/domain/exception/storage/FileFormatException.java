package com.employees.employees.domain.exception.storage;

public class FileFormatException extends RuntimeException{
    public FileFormatException(String message) {
        super(message);
    }

    public FileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
