package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.models.Diagnosis;
import org.example.medicalrecordproject.repositories.DiagnosisRepository;
import org.example.medicalrecordproject.services.contracts.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;

    @Autowired
    public DiagnosisServiceImpl(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    @Override
    public List<Diagnosis> getAllDiagnoses() {
        return diagnosisRepository.findAll();
    }

    @Override
    public Diagnosis getDiagnosisById(long id) {
        return diagnosisRepository.findById(id).orElse(null);
    }

    @Override
    public Diagnosis saveDiagnosis(Diagnosis diagnosis) {
        return diagnosisRepository.save(diagnosis);
    }

    @Override
    public void deleteDiagnosis(long id) {
        diagnosisRepository.deleteById(id);
    }

    @Override
    public void updateDiagnosis(long id, Diagnosis diagnosis) {
        Diagnosis existingDiagnosis = diagnosisRepository.findById(id).orElse(null);
        if (existingDiagnosis != null) {
            existingDiagnosis.setName(diagnosis.getName());
            existingDiagnosis.setDescription(diagnosis.getDescription());
            diagnosisRepository.save(existingDiagnosis);
        }
    }

}
