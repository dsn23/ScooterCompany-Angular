package com.example.demo.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
public class JWToken {
  public static final String JWT_USERNAME_CLAIM = "sub";
  public static final String JWT_USERID_CLAIM = "id";
  public static final String JWT_ADMIN_CLAIM = "admin";
  public static final String JWT_ATTRIBUTE_NAME = "tokeninfo";

  private String username = "test";
  private Long userId = Long.valueOf(30001);
  private boolean admin = false;

  public JWToken() {
  }

  public JWToken(String username, long userId, boolean admin) {
    this.username = username;
    this.userId = userId;
    this.admin = admin;
  }

  public String encode(String issuer, String passPhrase, int expiration) {
    Key key = getKey(passPhrase);

    String token = Jwts.builder()
      .claim(JWT_USERNAME_CLAIM, this.username)
      .claim(JWT_ADMIN_CLAIM, this.admin)
      .claim(JWT_USERID_CLAIM, this.userId)
      .setIssuer(issuer)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
      .signWith(key, SignatureAlgorithm.HS512)
      .compact();

    return token;
  }

  private static Key getKey(String passPhrase) {
    byte hmacKey[] = passPhrase.getBytes(StandardCharsets.UTF_8);
    Key key = new SecretKeySpec(hmacKey, SignatureAlgorithm.HS512.getJcaName());
    return key;
  }

  public static JWToken decode(String token, String passPhrase) {
    try {
      // Validate the token
      Key key = getKey(passPhrase);

      Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
      Claims claims = jws.getBody();

      JWToken jwToken = new JWToken(
        claims.get(JWT_USERNAME_CLAIM).toString(),
        Long.valueOf(claims.get(JWT_USERID_CLAIM).toString()),
        (boolean) claims.get(JWT_ADMIN_CLAIM)
      );

      return jwToken;
    }
    catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e){
      return null;
    }
  }
}
