package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.dtos.in.creation.AdminCreationDto;
import org.example.medicalrecordproject.dtos.out.creationresponse.AdminCreationResponseDto;
import org.example.medicalrecordproject.enums.UserRole;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.mappers.RegisterMapper;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.models.users.Admin;
import org.example.medicalrecordproject.models.users.User;
import org.example.medicalrecordproject.repositories.UserRepository;
import org.example.medicalrecordproject.services.contracts.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegisterMapper registerMapper;
    private final ValidationHelper validationHelper;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            RegisterMapper registerMapper,
                            ValidationHelper validationHelper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.registerMapper = registerMapper;
        this.validationHelper = validationHelper;
    }

    @Override
    public List<User> getAllAdmins() {
        return userRepository.findAllByRole(UserRole.ADMIN);
    }

    @Override
    public User getAdminById(long id) throws EntityNotFoundException {
        return userRepository.findByIdAndRole(id, UserRole.ADMIN)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + id + " not found."));
    }

    @Override
    public AdminCreationResponseDto createAdmin(AdminCreationDto dto, Timestamp timestamp) {
        Admin admin = registerMapper.toAdmin(dto);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setCreatedAt(timestamp);

        validationHelper.validateUserCreationData(admin, userRepository.existsByUsername(admin.getUsername()));

        saveAdmin(admin);
        return registerMapper.toAdminDto(admin);
    }

    @Override
    public User saveAdmin(User admin) {
        validationHelper.validateUserCreationData(admin, userRepository.existsByUsername(admin.getUsername()));

        admin.setRole(UserRole.ADMIN);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        return userRepository.save(admin);
    }

    @Override
    public void deleteAdmin(long id) {
        userRepository.findByIdAndRole(id, UserRole.ADMIN).ifPresent(userRepository::delete);
    }

    @Override
    public void updateAdmin(long id, User admin) throws EntityNotFoundException {
        validationHelper.validateUserCreationData(admin, userRepository.existsByUsername(admin.getUsername()));

        User existingAdmin = userRepository.findByIdAndRole(id, UserRole.ADMIN)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + id + " not found."));

        validationHelper.validateUsernameChange(admin.getUsername(), existingAdmin.getUsername(), userRepository.existsByUsername(admin.getUsername()));

        existingAdmin.setName(admin.getName());
        existingAdmin.setUsername(admin.getUsername());
        existingAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));

        saveAdmin(existingAdmin);
    }
}
