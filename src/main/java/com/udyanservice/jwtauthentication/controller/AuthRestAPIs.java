package com.udyanservice.jwtauthentication.controller;

import com.udyanservice.jwtauthentication.requestandResponce.request.LoginRequest;
import com.udyanservice.jwtauthentication.requestandResponce.request.SignUpRequest;
import com.udyanservice.jwtauthentication.security.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

  @Autowired AuthService authService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    return authService.authenticateUser(loginRequest);
  }

  @PostMapping("/signup")
  public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    return authService.registerUser(signUpRequest);
  }
}
