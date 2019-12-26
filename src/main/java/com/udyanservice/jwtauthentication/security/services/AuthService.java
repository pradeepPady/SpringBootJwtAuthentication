package com.udyanservice.jwtauthentication.security.services;

import com.udyanservice.jwtauthentication.model.Role;
import com.udyanservice.jwtauthentication.model.RoleName;
import com.udyanservice.jwtauthentication.model.User;
import com.udyanservice.jwtauthentication.repository.RoleRepository;
import com.udyanservice.jwtauthentication.repository.UserRepository;
import com.udyanservice.jwtauthentication.requestandResponce.request.LoginRequest;
import com.udyanservice.jwtauthentication.requestandResponce.request.SignUpRequest;
import com.udyanservice.jwtauthentication.requestandResponce.response.JwtResponse;
import com.udyanservice.jwtauthentication.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

public class AuthService {

  @Autowired AuthenticationManager authenticationManager;

  @Autowired UserRepository userRepository;

  @Autowired RoleRepository roleRepository;

  @Autowired PasswordEncoder encoder;

  @Autowired JwtProvider jwtProvider;

  public ResponseEntity<String> registerUser(SignUpRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return new ResponseEntity<String>("Username is already taken!", HttpStatus.BAD_REQUEST);
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return new ResponseEntity<String>("Email is already in use!", HttpStatus.BAD_REQUEST);
    }

    // Creating user's account
    User user =
        new User(
            signUpRequest.getName(),
            signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    strRoles.forEach(
        role -> {
          switch (role) {
            case "admin":
              Role adminRole =
                  roleRepository
                      .findByName(RoleName.ROLE_ADMIN)
                      .orElseThrow(
                          () -> new RuntimeException(" User Role not find."));
              roles.add(adminRole);

              break;
            case "manager":
              Role pmRole =
                  roleRepository
                      .findByName(RoleName.ROLE_MANAGER)
                      .orElseThrow(
                          () -> new RuntimeException("User Role not find."));
              roles.add(pmRole);

              break;
            default:
              Role userRole =
                  roleRepository
                      .findByName(RoleName.ROLE_USER)
                      .orElseThrow(
                          () -> new RuntimeException("User Role not find."));
              roles.add(userRole);
          }
        });

    user.setRoles(roles);
    userRepository.save(user);
    return ResponseEntity.ok().body("User registered successfully!");
  }

  public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = jwtProvider.generateJwtToken(authentication);
    return ResponseEntity.ok(new JwtResponse(jwt));
  }
}
