package net.miwashi.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.miwashi.edu.security.Credentials;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/session")
public class SessionController {

    private static final Logger LOGGER = LoggerFactory.getLogger( SessionController.class );

    @GetMapping(path="/auth", produces = "application/json")
    public ResponseEntity<?> authorizeWithGet(@NotNull @RequestParam(name = "credentials") String credentialsParam) {
        var credentials = jsonToCredentials(credentialsParam);
        LOGGER.info(credentials.toString());
        return ResponseEntity.ok("Returning JWT");
    }

    @GetMapping(path="/auth", produces = "application/json")
    public ResponseEntity<?> authorizeWithGet(@NotNull @RequestParam(name = "user") String user, @NotNull @RequestParam(name = "password") String password) {
        if(user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(password == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        var credentials = new Credentials(user, password);

        LOGGER.info(credentials.toString());
        return ResponseEntity.ok("Returning JWT");
    }

    @PostMapping(path="/auth", produces = "application/json")
    public ResponseEntity<?> authorizeWithPost(@RequestBody Credentials credentials) {
        LOGGER.info(credentials.toString());
        return ResponseEntity.ok("Returning JWT");
    }

    private Credentials jsonToCredentials(String credentialsJson) {
        Credentials credentials = null;
        try {
            credentials = (new ObjectMapper()).readValue(credentialsJson, Credentials.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Exception thrown in parsing json credentials!",e);
        } catch (Throwable e){
            LOGGER.error("Exception thrown in parsing json credentials!",e);
        }
        if(credentials==null){
            credentials = new Credentials();
        }
        return credentials;
    }
}

