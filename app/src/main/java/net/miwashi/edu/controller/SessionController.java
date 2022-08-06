package net.miwashi.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.miwashi.edu.security.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/session")
public class SessionController {

    private static final Logger LOGGER = LoggerFactory.getLogger( SessionController.class );

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;
/*
    @GetMapping(path = "/auth", params = {"user", "password"})
    public ResponseEntity<?> getIt(@RequestParam String user, @RequestParam String password){
        if(user == null || user.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(password == null || password.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var credentials = new Credentials(user, password);
        LOGGER.info("Attempting to authorize user: " + credentials.getUser());
        return ResponseEntity.ok("Returning JWT for user and password");
    }

    @GetMapping(path = "/auth", params = {"credentials"})
    public ResponseEntity<?> getIt(@RequestParam(name = "credentials") String credentialsParam){
        var credentials = jsonToCredentials(credentialsParam);
        if(credentials==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        LOGGER.info("Attempting to authorize user: " + credentials.getUser());
        return ResponseEntity.ok("Returning JWT for credentials");
    }

    @PostMapping(path="/auth", produces = "application/json")
    public ResponseEntity<?> authorizeWithPost(@RequestBody Credentials credentials) {
        LOGGER.info(credentials.toString());
        return ResponseEntity.ok("Returning JWT");
    }

    private Credentials jsonToCredentials(String credentialsJson) {
        if(credentialsJson==null || credentialsJson.isBlank()){
            return null;
        }
        Credentials credentials = null;
        try {
            credentials = (new ObjectMapper()).readValue(credentialsJson, Credentials.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Parsing Exception thrown when parsing json credentials!",e);
        } catch (Throwable e){
            LOGGER.error("Unknown Exception thrown when parsing json credentials!",e);
        }
        return credentials;
    }
*/

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}

