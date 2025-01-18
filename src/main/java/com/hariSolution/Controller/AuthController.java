package com.hariSolution.Controller;
//7.0 create authController

import com.hariSolution.DataTrasferObject.RegisterRequest;
import com.hariSolution.Repository.roleRepository;
import com.hariSolution.Repository.userRepository;
import com.hariSolution.entitiy.Role;
import com.hariSolution.entitiy.User;
import com.hariSolution.securityConfigur.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    //7.1 following variable object shall be imported for userRegistration

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final roleRepository RoleRepository;
    private final userRepository UserRepository;
    private final PasswordEncoder passwordEncoder;
    //7.2 create above object Constructors


    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, roleRepository roleRepository, userRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.RoleRepository = roleRepository;
        this.UserRepository = userRepository;
    }

    //7.3 create Registration API
    @PostMapping("/register")
    public ResponseEntity<String>register(@RequestBody RegisterRequest registerRequest) {
        //1. check already user data available or not in the database
        //based on databse data we have to check the bewlow data
        if(UserRepository.findByUsername(registerRequest.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("User already exists");
        }
        //if not previously we have to get the new user

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        //we need encode password
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        newUser.setPassword(encodedPassword);
      //convert to role name to roles entitiy and assign user
        Set<Role> roles = new HashSet<>();
        for(String roleName:registerRequest.getRoles()){
            Role role = RoleRepository.findByName(roleName).orElseThrow(()-> new RuntimeException("Role not found: "+ roleName));
            roles.add(role);
        }
        newUser.setRoles(roles);
        UserRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully");

    }
    //login API
    @PostMapping("/login")
    public ResponseEntity<String>login(@RequestBody User loginRequest) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        }catch (Exception e){
            System.out.println("Exception: "+ e);
        }
        //if tokon is ok we return to clint something

        String token=jwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(token);
    }

    //create the JwtAUTHENTICATIONFILTER class
}
