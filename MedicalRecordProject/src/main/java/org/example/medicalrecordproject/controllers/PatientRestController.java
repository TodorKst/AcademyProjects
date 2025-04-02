package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.out.GpPatientCountOutDto;
import org.example.medicalrecordproject.dtos.out.PatientOutDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.models.users.Patient;
import org.example.medicalrecordproject.services.contracts.MedicalVisitService;
import org.example.medicalrecordproject.services.contracts.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientRestController {

    private final PatientService patientService;
    private final MedicalVisitService medicalVisitService;

    @Autowired
    public PatientRestController(PatientService patientService,
                                 MedicalVisitService medicalVisitService) {
        this.patientService = patientService;
        this.medicalVisitService = medicalVisitService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public Patient getPatientById(@PathVariable long id) {
        try {
            return patientService.getPatientById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.savePatient(patient);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePatient(@PathVariable long id) {
        try {
            patientService.deletePatient(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updatePatient(@PathVariable long id, @RequestBody Patient patient) {
        try {
            patientService.updatePatient(id, patient);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/medical-visits")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN') or hasRole('PATIENT') and @authHelper.isPatientOwner(#id, authentication.name)")
    public List<MedicalVisit> getMedicalVisitsByPatientId(@PathVariable long id) {
        return medicalVisitService.getByPatientId(id);
    }

    @GetMapping("/by-diagnosis")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public List<PatientOutDto> getPatientsByDiagnosis(@RequestParam String diagnosis) {
        return patientService.getPatientsByDiagnosis(diagnosis);
    }

    @GetMapping("/by-gp")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public List<PatientOutDto> getPatientsByGp(@RequestParam Long gpId) {
        return patientService.getPatientsByGp(gpId);
    }

    @GetMapping("/count-by-gp")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public List<GpPatientCountOutDto> getPatientCountPerGp() {
        return patientService.countPatientsPerGp();
    }

    @GetMapping("/{id}/visits")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN') or hasRole('PATIENT') and @authHelper.isPatientOwner(#id, authentication.name)")
    public List<MedicalVisit> getVisitHistory(@PathVariable Long id) {
        return medicalVisitService.getByPatientId(id);
    }

}
