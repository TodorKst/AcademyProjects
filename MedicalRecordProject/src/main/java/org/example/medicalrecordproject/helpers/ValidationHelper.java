package org.example.medicalrecordproject.helpers;

import org.example.medicalrecordproject.exceptions.*;
import org.example.medicalrecordproject.models.Diagnosis;
import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.models.SickLeave;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;
import org.example.medicalrecordproject.models.users.User;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Component
public final class ValidationHelper {

    private ValidationHelper() {
    }

    public void validateUserCreationData(User user, boolean userExists) {
        validateUsernameLength(user.getUsername());
        checkUsernameUniqueness(userExists);
        validateNameLength(user.getName());
        validatePassword(user.getPassword());
    }

    public void validateDoctorCreationData(Doctor doctor, boolean userExists) {
        validateUserCreationData(doctor, userExists);

        validateDoctorSpecialties(doctor);

        validateDoctorSpecialties(doctor);
    }

    public void validatePatientCreationData(Patient patient, boolean userExists) {
        validateUserCreationData(patient, userExists);

        validateAssignedGp(patient);
    }

    public void validateMedicalVisitCreationData(MedicalVisit medicalVisit) {
        validateMedicalVisitEntities(medicalVisit);
        validateVisitDate(medicalVisit.getVisitDate());
        validateDiagnosesExist(medicalVisit.getDiagnoses());
        validateInsurancePayment(medicalVisit.getPatient());
    }

    public void validateSickLeaveCreationData(SickLeave sickLeave, boolean sickLeaveExists) {
        validateSickLeaveDates(sickLeave.getStartDate(), sickLeave.getEndDate());
        validateSickLeaveMedicalVisit(sickLeave.getMedicalVisit());
        validateSickLeaveUniqueness(sickLeaveExists);
    }

    public void validateAssignedGp(Patient patient) {
        if (patient.getGp() == null) {
            throw new InvalidGpAssignmentException("Patient must have a registered GP.");
        }
        if (patient.getGp().getIsGp() == null || !patient.getGp().getIsGp()) {
            throw new InvalidGpAssignmentException("Assigned GP is not a valid General Practitioner.");
        }
    }

    public void validateInsurancePayment(Patient patient) {
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

    public void validateVisitDate(LocalDateTime visitDate) {
        if (visitDate.isAfter(LocalDateTime.now())) {
            throw new InvalidMedicalVisitException("Medical visit date cannot be in the future.");
        }
    }

    public void validateMedicalVisitEntities(MedicalVisit visit) {
        if (visit.getDoctor() == null) {
            throw new InvalidMedicalVisitException("Medical visit must have an assigned doctor.");
        }
        if (visit.getPatient() == null) {
            throw new InvalidMedicalVisitException("Medical visit must have an assigned patient.");
        }
    }

    public void validateDiagnosesExist(Set<Diagnosis> diagnoses) {
        if (diagnoses != null) {
            for (Diagnosis d : diagnoses) {
                if (d == null || d.getId() == null) {
                    throw new InvalidDiagnosisReferenceException("All diagnoses must exist and have valid IDs.");
                }
            }
        }
    }

    public void validateSickLeaveDates(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidSickLeaveException("Sick leave start and end dates are required.");
        }
        if (startDate.after(endDate)) {
            throw new InvalidSickLeaveException("Sick leave start date cannot be after the end date.");
        }
    }

    public void validateSickLeaveUniqueness(boolean hasExistingSickLeave) {
        if (hasExistingSickLeave) {
            throw new InvalidSickLeaveException("A sick leave record already exists for this medical visit.");
        }
    }

    public void validateSickLeaveMedicalVisit(MedicalVisit medicalVisit) {
        if (medicalVisit == null) {
            throw new InvalidSickLeaveException("Sick leave must be associated with a medical visit.");
        }
    }

    public void validateDoctorSpecialties(Doctor doctor) {
        if (doctor.getSpecialties() == null || doctor.getSpecialties().isEmpty()) {
            throw new InvalidDoctorSpecialtiesException("Doctor must have at least one specialty.");
        }
        doctor.getSpecialties().forEach(specialty -> {
            if (specialty.getName() == null || specialty.getName().trim().isEmpty()) {
                throw new InvalidDoctorSpecialtiesException("Doctor's specialty must have a valid name.");
            }
        });
    }

    public void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new WeakPasswordException("Password must be at least 8 characters long.");
        }
    }

    public void checkUsernameUniqueness(boolean userExists) {
        if (userExists) {
            throw new InvalidUserCredentialException("Username already exists.");
        }
    }

    public void validateUsernameChange(String oldUsername, String newUsername, boolean userExists) {
        if (!oldUsername.equals(newUsername)) {
            checkUsernameUniqueness(userExists);
        }
        validateUsernameLength(newUsername);
    }

    public void validateUsernameLength(String username) {
        if (username == null || username.length() < 3 || username.length() > 50) {
            throw new InvalidUserCredentialException("Username must be at least 3 characters long and at most 50 characters long.");
        }
    }

    public void checkDiagnosisUniqueness(boolean diagnosisExists) {
        if (diagnosisExists) {
            throw new InvalidDiagnosisReferenceException("Diagnosis with name already exists.");
        }
    }

    public void validateNameLength(String name) {
        if (name == null || name.length() < 2 || name.length() > 100) {
            throw new InvalidUserCredentialException("Name must be at least 2 characters long and at most 100 characters long.");
        }
    }
}
