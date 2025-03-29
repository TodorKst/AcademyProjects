package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.helpers.AuthenticationHelper;
import org.example.medicalrecordproject.models.Diagnosis;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.User;
import org.example.medicalrecordproject.services.DiagnosisServiceImpl;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TestRestController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DiagnosisServiceImpl diagnosisServiceImpl;

//    @GetMapping("/api/diagnoses")
//    public ResponseEntity<List<Diagnosis>> getAllDiagnoses() {
//        List<Diagnosis> diagnoses = diagnosisServiceImpl.getAllDiagnoses();
//        return ResponseEntity.ok(diagnoses);
//    }

    @GetMapping("/test/{id}")
    public String test(@PathVariable long id) {
        User doctor = doctorService.getDoctorById(id);
        System.out.println(doctor.getRole().toString());
        System.out.println("----------------------------------------");
        AuthenticationHelper.throwIfNotAdmin(doctor);
        System.out.println(doctor.getRole());
        return "Test";
    }
}
