package org.example.medicalrecordproject.exceptions;

public class InsurancePaymentExpiredException extends RuntimeException {
    public InsurancePaymentExpiredException(String message) {
        super(message);
    }
}
