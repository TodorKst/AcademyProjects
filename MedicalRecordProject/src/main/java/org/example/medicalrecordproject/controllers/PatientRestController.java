package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.in.creation.PatientCreationDto;
import org.example.medicalrecordproject.dtos.out.GpPatientCountOutDto;
import org.example.medicalrecordproject.dtos.out.response.MedicalVisitResponseDto;
import org.example.medicalrecordproject.dtos.out.response.PatientResponseDto;
import org.example.medicalrecordproject.models.users.Patient;
import org.example.medicalrecordproject.services.contracts.MedicalVisitService;
import org.example.medicalrecordproject.services.contracts.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
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
    public List<PatientResponseDto> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public PatientResponseDto getPatientById(@PathVariable long id) {
        return patientService.getPatientByIdResponse(id);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public PatientResponseDto createPatient(@RequestBody PatientCreationDto dto) {
        return patientService.createPatient(dto, Timestamp.from(Instant.now()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePatient(@PathVariable long id) {
        patientService.deletePatient(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updatePatient(@PathVariable long id, @RequestBody Patient patient) {
        patientService.updatePatient(id, patient);
    }

    @GetMapping("/{id}/medical-visits")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN') or hasRole('PATIENT') and @authHelper.isPatientOwner(#id, authentication.name)")
    public List<MedicalVisitResponseDto> getMedicalVisitsByPatientId(@PathVariable long id) {
        return medicalVisitService.getByPatientId(id);
    }

    @GetMapping("/by-diagnosis")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public List<PatientResponseDto> getPatientsByDiagnosis(@RequestParam String diagnosis) {
        return patientService.getPatientsByDiagnosis(diagnosis);
    }

    @GetMapping("/by-gp")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public List<PatientResponseDto> getPatientsByGp(@RequestParam Long gpId) {
        return patientService.getPatientsByGp(gpId);
    }

    @GetMapping("/count-by-gp")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public List<GpPatientCountOutDto> getPatientCountPerGp() {
        return patientService.countPatientsPerGp();
    }

    @GetMapping("/{id}/visits")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN') or hasRole('PATIENT') and @authHelper.isPatientOwner(#id, authentication.name)")
    public List<MedicalVisitResponseDto> getVisitHistory(@PathVariable Long id) {
        return medicalVisitService.getByPatientId(id);
    }

}
