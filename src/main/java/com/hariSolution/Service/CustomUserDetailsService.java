package com.hariSolution.Service;
//step 4: 1.create below class

//2.impliment userDetailsService

import com.hariSolution.Repository.userRepository;
import com.hariSolution.entitiy.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
//3.connect to userRespoitory;

    private final userRepository UserRepository;

//4.create the constructor
    public CustomUserDetailsService(userRepository userRepository) {
        this.UserRepository = userRepository;
    }

//5. loadUserByUsername we have to provide our user information so create user object
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=UserRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("USER NOT FOUND"+username));
//6.map the roles to authorities
        //it used to verify the existing user information in database and after verification it will give the authendication

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),user
                .getRoles().stream().map(role->new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }

    // next create the JwltUtility class -->

    
}
