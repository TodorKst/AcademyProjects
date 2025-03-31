package org.example.medicalrecordproject.exceptions;

public class InvalidSickLeaveException extends RuntimeException {
    public InvalidSickLeaveException(String message) {
        super(message);
    }
}
