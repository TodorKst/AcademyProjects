package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.models.Diagnosis;
import org.example.medicalrecordproject.services.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TestRestController {

    @Autowired
    private DiagnosisService diagnosisService;

    @GetMapping("/api/diagnoses")
    public ResponseEntity<List<Diagnosis>> getAllDiagnoses() {
        List<Diagnosis> diagnoses = diagnosisService.getAllDiagnoses();
        return ResponseEntity.ok(diagnoses);
    }
}
