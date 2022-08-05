package net.miwashi.edu.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {

    private static final Logger LOGGER = LoggerFactory.getLogger( Credentials.class );
    private String user;
    private String password;

    public String toString(){
        return "User: " + user;
    }
}
