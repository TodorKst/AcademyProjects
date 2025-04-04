package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.dtos.in.creation.SickLeaveCreationDto;
import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.example.medicalrecordproject.dtos.out.MonthAndCountOutDto;
import org.example.medicalrecordproject.dtos.out.response.SickLeaveResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.helpers.mappers.EntityMapper;
import org.example.medicalrecordproject.models.SickLeave;
import org.example.medicalrecordproject.repositories.DoctorRepository;
import org.example.medicalrecordproject.repositories.SickLeaveRepository;
import org.example.medicalrecordproject.services.contracts.SickLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SickLeaveServiceImpl implements SickLeaveService {
    private final SickLeaveRepository sickLeaveRepository;
    private final DoctorRepository doctorRepository;
    private final ValidationHelper validationHelper;
    private final EntityMapper entityMapper;

    @Autowired
    public SickLeaveServiceImpl(SickLeaveRepository sickLeaveRepository,
                                DoctorRepository doctorRepository,
                                ValidationHelper validationHelper,
                                EntityMapper entityMapper) {
        this.sickLeaveRepository = sickLeaveRepository;
        this.doctorRepository = doctorRepository;
        this.validationHelper = validationHelper;
        this.entityMapper = entityMapper;
    }

    @Override
    public List<SickLeaveResponseDto> getAllSickLeaves() {
        return entityMapper.toSickLeaveDtoList(sickLeaveRepository.findAll());
    }

    @Override
    public SickLeave getSickLeaveById(long id) throws EntityNotFoundException {
        return sickLeaveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SickLeave"));
    }

    @Override
    public SickLeaveResponseDto getSickLeaveByIdResponse(long id) throws EntityNotFoundException {
        return entityMapper.toSickLeaveDto(getSickLeaveById(id));
    }

    @Override
    public SickLeave saveSickLeave(SickLeave sickLeave) {
        validationHelper.validateSickLeaveCreationData(sickLeave, sickLeaveRepository.existsById(sickLeave.getId()));

        return sickLeaveRepository.save(sickLeave);
    }

    @Override
    public SickLeaveResponseDto createSickLeave(SickLeaveCreationDto sickLeave, Timestamp createdAt) {
        SickLeave sickLeaveEntity = entityMapper.toSickLeave(sickLeave);
        sickLeaveEntity.setCreatedAt(createdAt);

        return entityMapper.toSickLeaveDto(sickLeaveRepository.save(sickLeaveEntity));
    }

    @Override
    public void deleteSickLeave(long id) {
        sickLeaveRepository.deleteById(id);
    }

    @Override
    public void updateSickLeave(long id, SickLeaveCreationDto dto) throws EntityNotFoundException {
        SickLeave sickLeave = entityMapper.toSickLeave(dto);
        SickLeave existingSickLeave = sickLeaveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SickLeave for: " + sickLeave.getMedicalVisit().getPatient().getName()));
        existingSickLeave.setStartDate(sickLeave.getStartDate());
        existingSickLeave.setEndDate(sickLeave.getEndDate());

        validationHelper.validateSickLeaveUpdateData(existingSickLeave);
        sickLeaveRepository.save(existingSickLeave);
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
