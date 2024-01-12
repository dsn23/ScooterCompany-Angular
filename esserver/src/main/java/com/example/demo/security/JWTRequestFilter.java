package com.example.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class JWTRequestFilter extends OncePerRequestFilter{
  private static final Set<String> SECURED_PATHS = Set.of("/scooters");

  @Value("${jwt.pass-phrase}")
  private String passPhrase;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
    String encodedToken = null;

    try{
      String path = req.getServletPath();

      if (HttpMethod.OPTIONS.matches(req.getMethod()) ||
        SECURED_PATHS.stream().noneMatch(path::startsWith)) {
        chain.doFilter(req, res);
        return;
      }

      encodedToken = req.getHeader(HttpHeaders.AUTHORIZATION);

      if(encodedToken == null) {
        throw new AuthenticationException("authentication problem");
      }

      encodedToken = encodedToken.replace("Bearer ", "");

      JWToken token = JWToken.decode(encodedToken, passPhrase);

      req.setAttribute(JWToken.JWT_ATTRIBUTE_NAME, token);
      chain.doFilter(req, res);

    }
    catch(AuthenticationException e ) {
      // aborting the chain
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You need to login first");
      return;
    }


  }
}
