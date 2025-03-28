package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.repositories.MedicalVisitRepository;
import org.example.medicalrecordproject.services.contracts.MedicalVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalVisitServiceImpl implements MedicalVisitService {

    private final MedicalVisitRepository medicalVisitRepository;

    @Autowired
    public MedicalVisitServiceImpl(MedicalVisitRepository medicalVisitRepository) {
        this.medicalVisitRepository = medicalVisitRepository;
    }

    @Override
    public List<MedicalVisit> getAllMedicalVisits() {
        return medicalVisitRepository.findAll();
    }

    @Override
    public MedicalVisit getMedicalVisitById(long id) {
        return medicalVisitRepository.findById(id).orElse(null);
    }

    @Override
    public MedicalVisit saveMedicalVisit(MedicalVisit medicalVisit) {
        return medicalVisitRepository.save(medicalVisit);
    }

    @Override
    public void deleteMedicalVisit(long id) {
        medicalVisitRepository.deleteById(id);
    }

    @Override
    public void updateMedicalVisit(long id, MedicalVisit medicalVisit) {
        MedicalVisit existingMedicalVisit = medicalVisitRepository.findById(id).orElse(null);
        if (existingMedicalVisit != null) {
            existingMedicalVisit.setDoctor(medicalVisit.getDoctor());
            existingMedicalVisit.setVisitDate(medicalVisit.getVisitDate());
            existingMedicalVisit.setPatient(medicalVisit.getPatient());
            existingMedicalVisit.setTreatment(medicalVisit.getTreatment());
            existingMedicalVisit.setDiagnoses(medicalVisit.getDiagnoses());
            medicalVisitRepository.save(existingMedicalVisit);
        }
    }
}
