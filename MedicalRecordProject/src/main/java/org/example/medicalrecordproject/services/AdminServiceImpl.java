package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.enums.UserRole;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.models.users.User;
import org.example.medicalrecordproject.repositories.UserRepository;
import org.example.medicalrecordproject.services.contracts.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public User saveAdmin(User admin) {
        admin.setRole(UserRole.ADMIN);
        ValidationHelper.checkUsernameUniqueness(userRepository.findByUsername(admin.getUsername()));
        ValidationHelper.validatePassword(admin.getPassword());
        ValidationHelper.validateNameLength(admin.getName());
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return userRepository.save(admin);
    }

    @Override
    public void deleteAdmin(long id) {
        userRepository.findByIdAndRole(id, UserRole.ADMIN).ifPresent(userRepository::delete);
    }

    @Override
    public void updateAdmin(long id, User updatedAdmin) throws EntityNotFoundException {
        User existingAdmin = userRepository.findByIdAndRole(id, UserRole.ADMIN)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + id + " not found."));
        ValidationHelper.validateUsernameChange(existingAdmin.getUsername(), updatedAdmin.getUsername(),
                userRepository.findByUsername(updatedAdmin.getUsername()));
        ValidationHelper.validatePassword(updatedAdmin.getPassword());
        ValidationHelper.validateNameLength(updatedAdmin.getName());
        existingAdmin.setName(updatedAdmin.getName());
        existingAdmin.setUsername(updatedAdmin.getUsername());
        ValidationHelper.validatePassword(updatedAdmin.getPassword());
        existingAdmin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));

        userRepository.save(existingAdmin);
    }
}
