package org.example.medicalrecordproject.helpers;

import org.example.medicalrecordproject.models.users.User;

public class AuthenticationHelper {

//    method works correctly
    public static boolean throwIfNotAdmin(User user) {
        if (!user.getRole().equals("ADMIN")) {
            throw new RuntimeException("You are not an admin");
        }
        return true;
    }

    public static boolean throwIfNotDoctor(User user) {
        if (!user.getRole().equals("DOCTOR")) {
            throw new RuntimeException("You are not a doctor");
        }
        return true;
    }

    public static boolean throwIfNotPatient(User user) {
        if (!user.getRole().equals("PATIENT")) {
            throw new RuntimeException("You are not a patient");
        }
        return true;
    }
}
