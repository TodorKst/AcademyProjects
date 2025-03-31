package org.example.medicalrecordproject.helpers;

import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.models.Diagnosis;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public final class ValidationHelper {

    private ValidationHelper() {
    }

    public static void validateAssignedGp(Patient patient) {
        if (patient.getGp() == null) {
            throw new ValidationException("Patient must have a registered GP.");
        }
        if (patient.getGp().getIsGp() == null || !patient.getGp().getIsGp()) {
            throw new ValidationException("Assigned GP is not a valid General Practitioner.");
        }
    }

    public static void validateInsurancePayment(Patient patient) {
        Date lastPayment = patient.getLastInsurancePayment();
        if (lastPayment == null) {
            throw new ValidationException("Patient has not made any insurance payment.");
        }
        LocalDate paymentDate = lastPayment.toLocalDate();
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        if (paymentDate.isBefore(sixMonthsAgo)) {
            throw new ValidationException("Patient's insurance payment is older than 6 months.");
        }
    }

    public static void validateVisitDate(LocalDateTime visitDate) {
        if (visitDate.isAfter(LocalDateTime.now())) {
            throw new ValidationException("Medical visit date cannot be in the future.");
        }
    }

    public static void validateMedicalVisitEntities(MedicalVisit visit) {
        if (visit.getDoctor() == null) {
            throw new ValidationException("Medical visit must have an assigned doctor.");
        }
        if (visit.getPatient() == null) {
            throw new ValidationException("Medical visit must have an assigned patient.");
        }
    }

    public static void validateDiagnosesExist(Set<Diagnosis> diagnoses) {
        if (diagnoses != null) {
            for (Diagnosis d : diagnoses) {
                if (d == null || d.getId() == null) {
                    throw new ValidationException("All diagnoses must exist and have valid IDs.");
                }
            }
        }
    }

    public static void validateSickLeaveDates(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new ValidationException("Sick leave start and end dates are required.");
        }
        if (startDate.after(endDate)) {
            throw new ValidationException("Sick leave start date cannot be after the end date.");
        }
    }

    public static void validateSickLeaveUniqueness(boolean hasExistingSickLeave) {
        if (hasExistingSickLeave) {
            throw new ValidationException("A sick leave record already exists for this medical visit.");
        }
    }

    public static void validateDoctorSpecialties(Doctor doctor) {
        if (doctor.getSpecialties() == null || doctor.getSpecialties().isEmpty()) {
            throw new ValidationException("Doctor must have at least one specialty.");
        }
        doctor.getSpecialties().forEach(specialty -> {
            if (specialty.getName() == null || specialty.getName().trim().isEmpty()) {
                throw new ValidationException("Doctor's specialty must have a valid name.");
            }
        });
    }

    public static void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long.");
        }
    }

    public static void validateUniqueUsername(boolean usernameExists) {
        if (usernameExists) {
            throw new ValidationException("Username already exists.");
        }
    }

    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
}
