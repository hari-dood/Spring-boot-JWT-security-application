package com.hariSolution.Controller;

import com.hariSolution.securityConfigur.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

//10.0 create user controller for give the access to resource
//10.1 role we have to give to application properties
@RestController
@RequestMapping("/api")
public class UserController {

//10.2 create jutil object to create autowire
    @Autowired
    private JwtUtil jwtUtil;
//10.3 take roles from application properties

    @Value("${role.admin}")
    private String rolAdmin;

    @Value("${role.user}")
    private String roleUser;

//10.4 create endpoint to access user protected resoures
    @GetMapping("/protected-data")
    public ResponseEntity<String> getprotectedData(@RequestHeader("Authorization") String token) {
      if(token != null && token.startsWith("Bearer")){
          String jwtToken=token.substring(7);//remove bearer from token
          try{
              if(jwtUtil.isTokenValid(jwtToken)){
                  String username=jwtUtil.extractUserName(jwtToken);//extract username from jwtToken

                  //extract roles from jwtToken
                  Set<String>roles=jwtUtil.extractRoles(jwtToken);

                  if(roles.contains(rolAdmin)){
                      return ResponseEntity.ok("Wellcome "+username+"  you can access "+ roles+ " -specific data");
                  }else if(roles.contains(roleUser)){
                      return ResponseEntity.ok("Wellcome "+username+"  you can access "+ roles+ " -specific data");
                  }
                  else {
                      return ResponseEntity.status(403).body("Access denied: you don't have sufficient access");
                  }
              }
          }catch (Exception ex){
              return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");
          }
      }
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization");
    }

}
