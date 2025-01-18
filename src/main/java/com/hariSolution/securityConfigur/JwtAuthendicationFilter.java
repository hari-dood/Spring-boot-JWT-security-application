package com.hariSolution.securityConfigur;

import com.hariSolution.Service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//7.0 JwtAuthendication filter class shall be created
//7.1 the oncePerDetailsService interface shall be extended
@Component
public class JwtAuthendicationFilter extends OncePerRequestFilter {
    //7.2 create the below object creation and create constructors
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService custoumUserDetailsService;

    public JwtAuthendicationFilter(JwtUtil jwtUtil, CustomUserDetailsService custoumUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.custoumUserDetailsService = custoumUserDetailsService;
    }
//implement the below-mentioned
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token=request.getHeader("Authorization");

    if (token !=null&&token.startsWith("Bearer ")) {

        token=token.substring(7);
        String username= jwtUtil.extractUserName(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails=custoumUserDetailsService.loadUserByUsername(username);
            if (jwtUtil.isTokenValid(token)){
                UsernamePasswordAuthenticationToken AuthToken=
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            AuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(AuthToken);
            }

        }
    }

     filterChain.doFilter(request, response);

    }
    //create security configur class
}
