package com.hariSolution.securityConfigur;

//step5: after complete the customUserDetailsService we have to craete the bewlow class

import com.hariSolution.Repository.userRepository;
import com.hariSolution.entitiy.Role;
import com.hariSolution.entitiy.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    //5.1 create secretKey
    private static final SecretKey secretKey= Keys.secretKeyFor(SignatureAlgorithm.HS512);

    //5.2 expiration time
    private  final int JwtExpirationMs=86400000;

    //5.3 userRepository import
    private userRepository UserRepository;
    //5.6 create constructor


    public JwtUtil(userRepository userRepository) {
       this.UserRepository = userRepository;
    }
    //5.6 generate token for create the method
    public String generateToken(String username){
        //5.6.1 we need user and role we have retrive from database
        Optional<User>user=UserRepository.findByUsername(username);

        Set<Role> roles=user.get().getRoles();
        //5.7 we have role and user has been retrive from dp

        //5.8 add roles to the token
        return Jwts.builder().setSubject(username).claim("roles",roles.stream()
                .map(role->role.getName()).collect(Collectors.joining(",")))
                .setIssuedAt(new Date()).setExpiration(new Date(System
                        .currentTimeMillis()+JwtExpirationMs)).signWith(secretKey).compact();


    }
    //5.9 extract user name

    public String extractUserName (String token){

        return  Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }
    //5.10 extract roles from token
    public Set<String> extractRoles(String token){
        String rolesString=Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("roles",String.class);

        return  Set.of(rolesString.split(","));

    }
    //5.10 token validation

    public boolean isTokenValid(String token){
        try{
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }

    }
    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // CREATE DATA TRANSFER OBJECT CLASS TO CREATE THE VARIABLE

}
