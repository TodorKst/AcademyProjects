package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.users.Admin;
import org.example.medicalrecordproject.models.users.User;

import java.util.List;

public interface AdminService {
    List<User> getAllAdmins();

    User getAdminById(long id) throws EntityNotFoundException;

    User saveAdmin(User admin);

    void deleteAdmin(long id);

    void updateAdmin(long id, User admin) throws EntityNotFoundException;
}
