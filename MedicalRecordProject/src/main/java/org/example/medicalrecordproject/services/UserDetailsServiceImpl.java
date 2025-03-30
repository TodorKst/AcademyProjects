package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.models.users.User;
import org.example.medicalrecordproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Trying to load user: " + username);

        try {
            org.example.medicalrecordproject.models.users.User user =
                    userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            System.out.println("Loaded user from DB:");
            System.out.println(" - username: " + user.getUsername());
            System.out.println(" - password: " + user.getPassword());
            System.out.println(" - role: " + user.getRole());

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
            );

        } catch (Exception e) {
            System.out.println("❌ Exception in loadUserByUsername:");
            e.printStackTrace();
            throw e;
        }
    }


}
