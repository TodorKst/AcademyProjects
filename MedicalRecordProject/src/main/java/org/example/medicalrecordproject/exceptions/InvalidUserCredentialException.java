package org.example.medicalrecordproject.exceptions;

public class InvalidUserCredentialException extends RuntimeException {
    public InvalidUserCredentialException(String message) {
        super(message);
    }
}
