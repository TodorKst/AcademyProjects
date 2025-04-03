package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.dtos.in.AdminCreationDto;
import org.example.medicalrecordproject.dtos.out.AdminCreationResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.users.User;

import java.sql.Timestamp;
import java.util.List;

public interface AdminService {
    List<User> getAllAdmins();

    User getAdminById(long id) throws EntityNotFoundException;

    AdminCreationResponseDto createAdmin(AdminCreationDto admin, Timestamp timestamp);

    User saveAdmin(User admin);

    void deleteAdmin(long id);

    void updateAdmin(long id, User admin) throws EntityNotFoundException;
}
