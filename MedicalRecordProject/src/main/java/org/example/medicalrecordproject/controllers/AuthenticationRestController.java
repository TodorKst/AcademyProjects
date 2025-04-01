package org.example.medicalrecordproject.controllers;

import jakarta.validation.Valid;
import org.example.medicalrecordproject.authentication.JwtAuthenticationResponse;
import org.example.medicalrecordproject.authentication.JwtTokenProvider;
import org.example.medicalrecordproject.dtos.in.AdminRegisterDto;
import org.example.medicalrecordproject.dtos.in.DoctorRegisterDto;
import org.example.medicalrecordproject.dtos.in.LoginDto;
import org.example.medicalrecordproject.dtos.in.PatientRegisterDto;
import org.example.medicalrecordproject.dtos.out.AdminRegisteredDto;
import org.example.medicalrecordproject.dtos.out.DoctorRegisteredDto;
import org.example.medicalrecordproject.dtos.out.PatientRegisteredDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.exceptions.InvalidUserCredentialException;
import org.example.medicalrecordproject.exceptions.WeakPasswordException;
import org.example.medicalrecordproject.services.contracts.AdminService;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.example.medicalrecordproject.services.contracts.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    public AdminRegisteredDto registerAdmin(@RequestBody @Valid AdminRegisterDto dto) {
        try {
            return adminService.createAdmin(dto, Timestamp.from(Instant.now()));
        } catch (InvalidUserCredentialException | WeakPasswordException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/register/doctor")
    public DoctorRegisteredDto registerDoctor(@RequestBody @Valid DoctorRegisterDto dto) {
        try {
            return doctorService.createDoctor(dto, Timestamp.from(Instant.now()));
        } catch (EntityNotFoundException | InvalidUserCredentialException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    //    return dto to avoid recursive calls to doctor then medical visit then doctor then medical visit....
    @PostMapping("/register/patient")
    public PatientRegisteredDto registerPatient(@RequestBody @Valid PatientRegisterDto dto) {
        try {
            return patientService.createPatient(dto, Timestamp.from(Instant.now()));
        } catch (EntityNotFoundException | InvalidUserCredentialException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
