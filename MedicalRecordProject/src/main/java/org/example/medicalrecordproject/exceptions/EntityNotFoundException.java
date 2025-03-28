package org.example.medicalrecordproject.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super("Entity not found: " + message);
    }
}
