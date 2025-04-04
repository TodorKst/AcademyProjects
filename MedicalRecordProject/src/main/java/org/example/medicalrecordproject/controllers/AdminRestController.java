package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.in.creation.AdminCreationDto;
import org.example.medicalrecordproject.dtos.out.response.AdminResponseDto;
import org.example.medicalrecordproject.models.users.Admin;
import org.example.medicalrecordproject.services.contracts.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/admins")
// allows only users with the ADMIN role to access all methods in this controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminRestController {

    private final AdminService adminService;

    @Autowired
    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping()
    public List<AdminResponseDto> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/{id}")
    public AdminResponseDto getAdminById(@PathVariable long id) {
        return adminService.getAdminByIdResponse(id);
    }

    @PostMapping()
    public AdminResponseDto createAdmin(@RequestBody AdminCreationDto admin) {
        return adminService.createAdmin(admin, Timestamp.from(Instant.now()));
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable long id) {
        adminService.deleteAdmin(id);
    }

    @PutMapping("/{id}")
    public AdminResponseDto updateAdmin(@PathVariable long id, @RequestBody Admin admin) {
        return adminService.updateAdmin(id, admin);
    }


}
