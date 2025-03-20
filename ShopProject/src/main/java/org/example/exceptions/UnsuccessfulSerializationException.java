package org.example.exceptions;

public class UnsuccessfulSerializationException extends RuntimeException {
    public UnsuccessfulSerializationException(String message) {
        super(message);
    }
}
