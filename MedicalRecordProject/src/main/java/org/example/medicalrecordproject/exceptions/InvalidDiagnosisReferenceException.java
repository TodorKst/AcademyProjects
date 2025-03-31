package org.example.medicalrecordproject.exceptions;

public class InvalidDiagnosisReferenceException extends RuntimeException {
    public InvalidDiagnosisReferenceException(String message) {
        super(message);
    }
}
