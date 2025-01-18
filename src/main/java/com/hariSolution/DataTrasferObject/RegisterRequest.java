package com.hariSolution.DataTrasferObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;
//6.0 create the requestRegistration class and include the variable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    //6.1 create variable
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("roles")
    private Set<String> roles;// set of roles to be passed to request
}

//create authController -->next step
