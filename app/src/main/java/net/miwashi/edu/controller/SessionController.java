package net.miwashi.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.miwashi.edu.security.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/session")
public class SessionController {//17.0.2-oracle 11.0.14-zulu

    private static final Logger LOGGER = LoggerFactory.getLogger( SessionController.class );

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
}

