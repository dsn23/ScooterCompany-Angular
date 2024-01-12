package com.example.demo.rest;

import com.example.demo.models.User;
import com.example.demo.security.JWToken;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authenticate")
public class AuthenticateController extends globalConfiguration{
  @Autowired
  private JWToken tokenGenerator;

  @Value("${jwt.pass-phrase}")
  private String passPhrase;

  @Value("${jwt.duration-of-validity}")
  private int tokenDurationOfValidity;

  @Value("${jwt.issuer}")
  private String issuer;



  @PostMapping("login")
  public ResponseEntity<User> saveScooter(@RequestBody ObjectNode signOnInfo) {
    String userEmail = signOnInfo.get("email").asText();
    String password = signOnInfo.get("password").asText();

    if(userEmail.substring(0,userEmail.indexOf("@")).equals(password)){
      User user = new User(userEmail);
      String tokenString = tokenGenerator.encode(this.issuer, this.passPhrase, this.tokenDurationOfValidity);

      return ResponseEntity.accepted()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenString).body(user);

    }
    else{
      throw new UnAuthorizedException("Cannot authenticate user by email=" + userEmail + " and password=" + password);
    }
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public static class UnAuthorizedException extends RuntimeException{
    public UnAuthorizedException(String message){
      super(message);
    }
  }
}
