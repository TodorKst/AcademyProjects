//package org.example.medicalrecordproject.controllers;
//
//import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
//import org.example.medicalrecordproject.models.users.Admin;
//import org.example.medicalrecordproject.services.contracts.AdminService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admins")
//public class AdminRestController {
//
//    private final AdminService adminService;
//
//    @Autowired
//    public AdminRestController(AdminService adminService) {
//        this.adminService = adminService;
//    }
//
//    @GetMapping()
//    public List<Admin> getAllAdmins() {
//        return adminService.getAllAdmins();
//    }
//
//    @GetMapping("/{id}")
//    public Admin getAdminById(@PathVariable long id) {
//        try {
//            return adminService.getAdminById(id);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
//
//    @PostMapping()
//    public Admin saveAdmin(@RequestBody Admin admin) {
//        return adminService.saveAdmin(admin);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteAdmin(@PathVariable long id) {
//        try {
//            adminService.deleteAdmin(id);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
//
//    @PutMapping("/{id}")
//    public void updateAdmin(@PathVariable long id, @RequestBody Admin admin) {
//        try {
//            adminService.updateAdmin(id, admin);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
//
//
//}
