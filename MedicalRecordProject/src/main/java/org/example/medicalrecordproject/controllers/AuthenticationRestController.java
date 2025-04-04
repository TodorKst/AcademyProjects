package org.example.medicalrecordproject.controllers;

import jakarta.validation.Valid;
import org.example.medicalrecordproject.authentication.JwtAuthenticationResponse;
import org.example.medicalrecordproject.authentication.JwtTokenProvider;
import org.example.medicalrecordproject.dtos.in.LoginDto;
import org.example.medicalrecordproject.dtos.in.creation.AdminCreationDto;
import org.example.medicalrecordproject.dtos.in.creation.PatientCreationDto;
import org.example.medicalrecordproject.dtos.out.response.AdminResponseDto;
import org.example.medicalrecordproject.dtos.out.response.DoctorResponseDto;
import org.example.medicalrecordproject.dtos.out.response.PatientResponseDto;
import org.example.medicalrecordproject.services.contracts.AdminService;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.example.medicalrecordproject.services.contracts.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AdminService adminService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager,
                                        JwtTokenProvider tokenProvider,
                                        AdminService adminService,
                                        DoctorService doctorService,
                                        PatientService patientService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.adminService = adminService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @PostMapping("/register/admin")
    public AdminResponseDto registerAdmin(@RequestBody @Valid AdminCreationDto dto) {
        return adminService.createAdmin(dto, Timestamp.from(Instant.now()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/doctor")
    public DoctorResponseDto registerDoctor(@RequestBody @Valid org.example.medicalrecordproject.dtos.in.creation.DoctorCreationDto dto) {
        return doctorService.createDoctor(dto, Timestamp.from(Instant.now()));
    }

    //    return dto to avoid recursive calls to doctor then medical visit then doctor then medical visit...
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/patient")
    public PatientResponseDto registerPatient(@RequestBody @Valid PatientCreationDto dto) {
        return patientService.createPatient(dto, Timestamp.from(Instant.now()));
    }
}
