package org.example.medicalrecordproject.helpers;

import org.example.medicalrecordproject.exceptions.InvalidDiagnosisReferenceException;
import org.example.medicalrecordproject.exceptions.InvalidDoctorSpecialtiesException;
import org.example.medicalrecordproject.exceptions.InvalidGpAssignmentException;
import org.example.medicalrecordproject.exceptions.InsurancePaymentExpiredException;
import org.example.medicalrecordproject.exceptions.InvalidMedicalVisitException;
import org.example.medicalrecordproject.exceptions.InvalidSickLeaveException;
import org.example.medicalrecordproject.exceptions.InvalidUserCredentialException;
import org.example.medicalrecordproject.exceptions.WeakPasswordException;
import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.models.Diagnosis;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public final class ValidationHelper {

    private ValidationHelper() {
    }

    public static void validateAssignedGp(Patient patient) {
        if (patient.getGp() == null) {
            throw new InvalidGpAssignmentException("Patient must have a registered GP.");
        }
        if (patient.getGp().getIsGp() == null || !patient.getGp().getIsGp()) {
            throw new InvalidGpAssignmentException("Assigned GP is not a valid General Practitioner.");
        }
    }

    public static void validateInsurancePayment(Patient patient) {
        Date lastPayment = patient.getLastInsurancePayment();
        if (lastPayment == null) {
            throw new InsurancePaymentExpiredException("Patient has not made any insurance payment.");
        }
        LocalDate paymentDate = lastPayment.toLocalDate();
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        if (paymentDate.isBefore(sixMonthsAgo)) {
            throw new InsurancePaymentExpiredException("Patient's insurance payment is older than 6 months.");
        }
    }

    public static void validateVisitDate(LocalDateTime visitDate) {
        if (visitDate.isAfter(LocalDateTime.now())) {
            throw new InvalidMedicalVisitException("Medical visit date cannot be in the future.");
        }
    }

    public static void validateMedicalVisitEntities(MedicalVisit visit) {
        if (visit.getDoctor() == null) {
            throw new InvalidMedicalVisitException("Medical visit must have an assigned doctor.");
        }
        if (visit.getPatient() == null) {
            throw new InvalidMedicalVisitException("Medical visit must have an assigned patient.");
        }
    }

    public static void validateDiagnosesExist(Set<Diagnosis> diagnoses) {
        if (diagnoses != null) {
            for (Diagnosis d : diagnoses) {
                if (d == null || d.getId() == null) {
                    throw new InvalidDiagnosisReferenceException("All diagnoses must exist and have valid IDs.");
                }
            }
        }
    }

    public static void validateSickLeaveDates(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidSickLeaveException("Sick leave start and end dates are required.");
        }
        if (startDate.after(endDate)) {
            throw new InvalidSickLeaveException("Sick leave start date cannot be after the end date.");
        }
    }

    public static void validateSickLeaveUniqueness(boolean hasExistingSickLeave) {
        if (hasExistingSickLeave) {
            throw new InvalidSickLeaveException("A sick leave record already exists for this medical visit.");
        }
    }

    public static void validateSickLeaveMedicalVisit(MedicalVisit medicalVisit) {
        if (medicalVisit == null) {
            throw new InvalidSickLeaveException("Sick leave must be associated with a medical visit.");
        }
    }

    public static void validateDoctorSpecialties(Doctor doctor) {
        if (doctor.getSpecialties() == null || doctor.getSpecialties().isEmpty()) {
            throw new InvalidDoctorSpecialtiesException("Doctor must have at least one specialty.");
        }
        doctor.getSpecialties().forEach(specialty -> {
            if (specialty.getName() == null || specialty.getName().trim().isEmpty()) {
                throw new InvalidDoctorSpecialtiesException("Doctor's specialty must have a valid name.");
            }
        });
    }

    public static void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new WeakPasswordException("Password must be at least 8 characters long.");
        }
    }

    public static void checkUsernameUniqueness(Optional<?> userOptional) {
        if (userOptional.isPresent()) {
            throw new InvalidUserCredentialException("Username already exists.");
        }
    }

    public static void validateUsernameChange(String oldUsername, String newUsername, Optional<?> userOptional) {
        if (!oldUsername.equals(newUsername)) {
            checkUsernameUniqueness(userOptional);
        }
    }

    public static void checkDiagnosisUniqueness(Optional<?> diagnosisOptional) {
        if (diagnosisOptional.isPresent()) {
            throw new InvalidDiagnosisReferenceException("Diagnosis with name already exists.");
        }
    }
}
