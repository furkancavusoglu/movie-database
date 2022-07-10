package springsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springsecurityjwt.requests.AuthenticationRequest;
import springsecurityjwt.responses.AuthenticationResponse;
import springsecurityjwt.service.MyUserDetailsService;
import springsecurityjwt.util.JwtUtil;

@RestController
public class HelloController {

    @Autowired
    JwtUtil util;
    @Autowired
    MyUserDetailsService service;

    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("/hello")
    public String hello() {
        return "Hello world";
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponse> getToken(@RequestBody AuthenticationRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            System.out.println("Bad Credentials :" + e);
        }

        final UserDetails userDetails = service.loadUserByUsername(request.getUsername());
        String token = util.generateToken(userDetails);
        return new ResponseEntity<>(new AuthenticationResponse(token), HttpStatus.OK);
    }
}
