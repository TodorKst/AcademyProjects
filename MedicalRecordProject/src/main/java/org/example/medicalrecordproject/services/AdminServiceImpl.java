package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.dtos.in.creation.AdminCreationDto;
import org.example.medicalrecordproject.dtos.out.response.AdminResponseDto;
import org.example.medicalrecordproject.enums.UserRole;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.helpers.mappers.RegisterMapper;
import org.example.medicalrecordproject.models.users.Admin;
import org.example.medicalrecordproject.models.users.User;
import org.example.medicalrecordproject.repositories.AdminRepository;
import org.example.medicalrecordproject.services.contracts.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegisterMapper registerMapper;
    private final ValidationHelper validationHelper;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository,
                            PasswordEncoder passwordEncoder,
                            RegisterMapper registerMapper,
                            ValidationHelper validationHelper) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.registerMapper = registerMapper;
        this.validationHelper = validationHelper;
    }

    @Override
    public List<AdminResponseDto> getAllAdmins() {
        return registerMapper.toAdminDtoList(adminRepository.findAll());
    }

    @Override
    public Admin getAdminById(long id) throws EntityNotFoundException {
        return adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + id + " not found."));
    }

    @Override
    public AdminResponseDto getAdminByIdResponse(long id) throws EntityNotFoundException {
        return registerMapper.toAdminDto(getAdminById(id));
    }

    @Override
    public AdminResponseDto createAdmin(AdminCreationDto dto, Timestamp timestamp) {
        Admin admin = registerMapper.toAdmin(dto);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setCreatedAt(timestamp);

        validationHelper.validateUserCreationData(admin, adminRepository.existsByUsername(admin.getUsername()));

        saveAdmin(admin);
        return registerMapper.toAdminDto(admin);
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        validationHelper.validateUserCreationData(admin, adminRepository.existsByUsername(admin.getUsername()));

        admin.setRole(UserRole.ADMIN);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        return adminRepository.save(admin);
    }

    @Override
    public void deleteAdmin(long id) {
        adminRepository.findById(id).ifPresent(adminRepository::delete);
    }

    @Override
    public void updateAdmin(long id, User admin) throws EntityNotFoundException {
        validationHelper.validateUserCreationData(admin, adminRepository.existsByUsername(admin.getUsername()));

        User existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + id + " not found."));

        validationHelper.validateUsernameChange(admin.getUsername(), existingAdmin.getUsername(), adminRepository.existsByUsername(admin.getUsername()));

        existingAdmin.setName(admin.getName());
        existingAdmin.setUsername(admin.getUsername());
        existingAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));

        saveAdmin(existingAdmin);
    }
}
