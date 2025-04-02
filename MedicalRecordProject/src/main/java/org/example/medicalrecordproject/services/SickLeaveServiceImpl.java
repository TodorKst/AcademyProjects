package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.example.medicalrecordproject.dtos.out.MonthAndCountOutDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.models.SickLeave;
import org.example.medicalrecordproject.repositories.DoctorRepository;
import org.example.medicalrecordproject.repositories.SickLeaveRepository;
import org.example.medicalrecordproject.services.contracts.SickLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SickLeaveServiceImpl implements SickLeaveService {
    private final SickLeaveRepository sickLeaveRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public SickLeaveServiceImpl(SickLeaveRepository sickLeaveRepository,
                                DoctorRepository doctorRepository) {
        this.sickLeaveRepository = sickLeaveRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<SickLeave> getAllSickLeaves() {
        return sickLeaveRepository.findAll();
    }

    @Override
    public SickLeave getSickLeaveById(long id) throws EntityNotFoundException {
        return sickLeaveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SickLeave"));
    }

    @Override
    public SickLeave saveSickLeave(SickLeave sickLeave) {
        ValidationHelper.validateSickLeaveMedicalVisit(sickLeave.getMedicalVisit());
        ValidationHelper.validateSickLeaveDates(sickLeave.getStartDate(), sickLeave.getEndDate());
        ValidationHelper.validateSickLeaveUniqueness(sickLeaveRepository.existsByMedicalVisitId(sickLeave.getMedicalVisit().getId()));
        return sickLeaveRepository.save(sickLeave);
    }

    @Override
    public void deleteSickLeave(long id) {
        sickLeaveRepository.deleteById(id);
    }

    @Override
    public SickLeave updateSickLeave(long id, SickLeave sickLeave) throws EntityNotFoundException {
        SickLeave existingSickLeave = sickLeaveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SickLeave for: " + sickLeave.getMedicalVisit().getPatient().getName()));
        existingSickLeave.setStartDate(sickLeave.getStartDate());
        existingSickLeave.setEndDate(sickLeave.getEndDate());
        return saveSickLeave(existingSickLeave);
    }

    @Override
    public MonthAndCountOutDto getMonthWithMostSickLeaves() {
        Object[] row = sickLeaveRepository.getMonthWithMostSickLeaves().get(0);
        return new MonthAndCountOutDto((Integer) row[0], (Long) row[1]);
    }

    @Override
    public List<DoctorStatOutDto> getDoctorsWithMostSickLeaves() {
        return sickLeaveRepository.getTopDoctorsBySickLeaves()
                .stream()
                .map(row -> {
                    Long doctorId = (Long) row[0];
                    Long count = (Long) row[1];
                    String name = doctorRepository.findById(doctorId)
                            .orElseThrow(() -> new EntityNotFoundException("Doctor")).getName();
                    return new DoctorStatOutDto(doctorId, name, count);
                })
                .collect(Collectors.toList());
    }
}
