//package org.example.medicalrecordproject.services;
//
//import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
//import org.example.medicalrecordproject.models.users.Admin;
//import org.example.medicalrecordproject.repositories.AdminRepository;
//import org.example.medicalrecordproject.services.contracts.AdminService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class AdminServiceImpl implements AdminService {
//
//    private final AdminRepository adminRepository;
//
//    @Autowired
//    public AdminServiceImpl(AdminRepository adminRepository) {
//        this.adminRepository = adminRepository;
//    }
//
//    @Override
//    public List<Admin> getAllAdmins() {
//        return adminRepository.findAll();
//    }
//
//    @Override
//    public Admin getAdminById(long id) throws EntityNotFoundException {
//        return adminRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Admin"));
//    }
//
//    @Override
//    public Admin saveAdmin(Admin admin) {
//        return adminRepository.save(admin);
//    }
//
//    @Override
//    public void deleteAdmin(long id) {
//        adminRepository.deleteById(id);
//    }
//
//    @Override
//    public void updateAdmin(long id, Admin admin) throws EntityNotFoundException {
//        Admin existingAdmin = adminRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException(admin.getName()));
//        existingAdmin.setName(admin.getName());
//        existingAdmin.setUsername(admin.getUsername());
//        existingAdmin.setPassword(admin.getPassword());
//        adminRepository.save(existingAdmin);
//    }
//
//}
