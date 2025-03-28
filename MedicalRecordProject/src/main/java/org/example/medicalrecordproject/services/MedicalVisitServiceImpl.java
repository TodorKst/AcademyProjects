package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.repositories.MedicalVisitRepository;
import org.example.medicalrecordproject.services.contracts.MedicalVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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
    public MedicalVisit getMedicalVisitById(long id) throws EntityNotFoundException {
        return medicalVisitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MedicalVisit"));
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
    public void updateMedicalVisit(long id, MedicalVisit medicalVisit) throws EntityNotFoundException {
        MedicalVisit existingMedicalVisit = medicalVisitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MedicalVisit for: " + medicalVisit.getPatient().getName()));
        if (existingMedicalVisit != null) {
            existingMedicalVisit.setDoctor(medicalVisit.getDoctor());
            existingMedicalVisit.setVisitDate(medicalVisit.getVisitDate());
            existingMedicalVisit.setPatient(medicalVisit.getPatient());
            existingMedicalVisit.setTreatment(medicalVisit.getTreatment());
            existingMedicalVisit.setDiagnoses(medicalVisit.getDiagnoses());
            medicalVisitRepository.save(existingMedicalVisit);
        }
    }

    @Override
    public List<MedicalVisit> getByPatientId(long id) {
        return medicalVisitRepository.findByPatientId(id);
    }

    @Override
    public List<MedicalVisit> getByDoctorId(long id) {
        return medicalVisitRepository.findByDoctorId(id);
    }

    @Override
    public List<MedicalVisit> getByVisitDate(String date) {
        LocalDateTime date1 = LocalDateTime.from(Instant.parse(date));
        return medicalVisitRepository.findByVisitDate(date1);
    }

    @Override
    public List<MedicalVisit> getByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return medicalVisitRepository.findByVisitDateBetween(startDate, endDate);
    }
}
