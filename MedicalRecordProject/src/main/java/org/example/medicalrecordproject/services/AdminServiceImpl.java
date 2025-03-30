package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.enums.UserRole;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.users.User;
import org.example.medicalrecordproject.repositories.UserRepository;
import org.example.medicalrecordproject.services.contracts.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        admin.setPassword(passwordEncoder.encode(admin.getPassword())); // Ensure password is encoded
        return userRepository.save(admin);
    }

    @Override
    public void deleteAdmin(long id) {
        Optional<User> adminOpt = userRepository.findByIdAndRole(id, UserRole.ADMIN);
        adminOpt.ifPresent(userRepository::delete);
    }

    @Override
    public void updateAdmin(long id, User updatedAdmin) throws EntityNotFoundException {
        User existingAdmin = userRepository.findByIdAndRole(id, UserRole.ADMIN)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + id + " not found."));

        existingAdmin.setName(updatedAdmin.getName());
        existingAdmin.setUsername(updatedAdmin.getUsername());

        if (!updatedAdmin.getPassword().isBlank()) {
            existingAdmin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));
        }

        userRepository.save(existingAdmin);
    }
}
