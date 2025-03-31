package org.example.medicalrecordproject.controllers;

import jakarta.validation.Valid;
import org.example.medicalrecordproject.authentication.JwtAuthenticationResponse;
import org.example.medicalrecordproject.authentication.JwtTokenProvider;
import org.example.medicalrecordproject.dtos.in.AdminRegisterDto;
import org.example.medicalrecordproject.dtos.in.DoctorRegisterDto;
import org.example.medicalrecordproject.dtos.in.LoginDto;
import org.example.medicalrecordproject.dtos.in.PatientRegisterDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.exceptions.InvalidUserCredentialException;
import org.example.medicalrecordproject.exceptions.WeakPasswordException;
import org.example.medicalrecordproject.helpers.RegisterMapper;
import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.models.users.Admin;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;
import org.example.medicalrecordproject.repositories.DoctorRepository;
import org.example.medicalrecordproject.repositories.SpecialtyRepository;
import org.example.medicalrecordproject.services.contracts.AdminService;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.example.medicalrecordproject.services.contracts.PatientService;
import org.example.medicalrecordproject.services.contracts.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AdminService adminService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final RegisterMapper registerMapper;
    private final SpecialtyService specialtyService;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager,
                                        JwtTokenProvider tokenProvider,
                                        AdminService adminService,
                                        DoctorService doctorService,
                                        PatientService patientService,
                                        RegisterMapper registerMapper,
                                        SpecialtyService specialtyService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.adminService = adminService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.registerMapper = registerMapper;

        this.specialtyService = specialtyService;
    }

    // 🔐 LOGIN
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

    // 🔧 TEST
    @GetMapping
    public String getHello() {
        return "Hello";
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody @Valid AdminRegisterDto dto) {
        try {
            Admin admin = registerMapper.toAdmin(dto);
            admin.setCreatedAt(Timestamp.from(Instant.now()));
            adminService.saveAdmin(admin);
            return ResponseEntity.ok("Admin registered.");
        } catch (InvalidUserCredentialException | WeakPasswordException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/register/doctor")
    public Doctor registerDoctor(@RequestBody @Valid DoctorRegisterDto dto) {
        Doctor doctor = null;
        try {
            doctor = registerMapper.toDoctor(dto);

            Set<Specialty> specialties = dto.getSpecialties().stream()
                    .map(specialtyService::getSpecialtyByName)
                    .collect(Collectors.toSet());

            doctor.setSpecialties(specialties);
            doctor.setCreatedAt(Timestamp.from(Instant.now()));

            doctorService.saveDoctor(doctor);
        } catch (EntityNotFoundException | InvalidUserCredentialException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return doctor;
    }

    @PostMapping("/register/patient")
    public Patient registerPatient(@RequestBody @Valid PatientRegisterDto dto) {
        try {
            Patient patient = registerMapper.toPatient(dto);

            Doctor gp = doctorService.getDoctorById(dto.getGpId());
            patient.setGp(gp);
            patient.setCreatedAt(Timestamp.from(Instant.now()));

            patientService.savePatient(patient);
            return patient;
        } catch (EntityNotFoundException | InvalidUserCredentialException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
