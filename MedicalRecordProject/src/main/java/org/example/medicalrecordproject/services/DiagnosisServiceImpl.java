package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.dtos.out.DiagnosisStatOutDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.models.Diagnosis;
import org.example.medicalrecordproject.repositories.DiagnosisRepository;
import org.example.medicalrecordproject.services.contracts.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final ValidationHelper validationHelper;

    @Autowired
    public DiagnosisServiceImpl(DiagnosisRepository diagnosisRepository,
                                ValidationHelper validationHelper) {
        this.diagnosisRepository = diagnosisRepository;
        this.validationHelper = validationHelper;
    }

    @Override
    public List<Diagnosis> getAllDiagnoses() {
        return diagnosisRepository.findAll();
    }

    @Override
    public Diagnosis getDiagnosisById(long id) throws EntityNotFoundException {
        return diagnosisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Diagnosis"));
    }

    @Override
    public Diagnosis saveDiagnosis(Diagnosis diagnosis) {
        validationHelper.checkDiagnosisUniqueness(diagnosisRepository.existsByName(diagnosis.getName()));

        return diagnosisRepository.save(diagnosis);
    }

    @Override
    public void deleteDiagnosis(long id) {
        diagnosisRepository.deleteById(id);
    }

    @Override
    public void updateDiagnosis(long id, Diagnosis diagnosis) throws EntityNotFoundException {
        Diagnosis existingDiagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(diagnosis.getName()));

        validationHelper.validateUsernameChange(diagnosis.getName(), existingDiagnosis.getName(),
                diagnosisRepository.existsByName(diagnosis.getName()));

        existingDiagnosis.setName(diagnosis.getName());
        existingDiagnosis.setDescription(diagnosis.getDescription());
        diagnosisRepository.save(existingDiagnosis);
    }

    @Override
    public List<DiagnosisStatOutDto> getMostCommonDiagnoses() {
        return diagnosisRepository.findMostCommonDiagnoses()
                .stream()
                .map(row -> new DiagnosisStatOutDto((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }
}
