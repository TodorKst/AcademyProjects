package org.example.medicalrecordproject.helpers;

import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.models.SickLeave;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.example.medicalrecordproject.services.contracts.MedicalVisitService;
import org.example.medicalrecordproject.services.contracts.PatientService;
import org.example.medicalrecordproject.services.contracts.SickLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("authHelper")
public class AuthHelper {

    private final MedicalVisitService medicalVisitService;
    private final PatientService patientService;
    private final SickLeaveService sickLeaveService;

    @Autowired
    public AuthHelper(MedicalVisitService medicalVisitService,
                      PatientService patientService,
                      SickLeaveService sickLeaveService) {
        this.medicalVisitService = medicalVisitService;
        this.patientService = patientService;
        this.sickLeaveService = sickLeaveService;
    }

    public boolean isOwnerOfVisit(Long visitId, String username) {
        MedicalVisit visit = medicalVisitService.getMedicalVisitById(visitId);
        Doctor doctor = visit.getDoctor();
        return doctor != null && doctor.getUsername().equals(username);
    }

    public boolean isPatientOwner(Long patientId, String username) {
        Patient patient = patientService.getPatientById(patientId);
        return patient.getUsername().equals(username);
    }

    public boolean isGpOfPatient(Long patientId, String doctorUsername) {
        Patient patient = patientService.getPatientById(patientId);
        Doctor gp = patient.getGp();
        return gp != null && gp.getUsername().equals(doctorUsername);
    }

    public boolean isPatientOwnerOfSickLeave(Long sickLeaveId, String username) {
        SickLeave sickLeave = sickLeaveService.getSickLeaveById(sickLeaveId);
        return sickLeave.getMedicalVisit().getPatient().getUsername().equals(username);
    }
}
