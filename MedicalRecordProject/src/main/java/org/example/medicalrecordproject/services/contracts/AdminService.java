package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.models.users.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> getAllAdmins();

    Admin getAdminById(long id);

    Admin saveAdmin(Admin admin);

    void deleteAdmin(long id);

    void updateAdmin(long id, Admin admin);
}
