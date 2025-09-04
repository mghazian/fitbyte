package com.coffeeteam.fitbyte.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthRequest {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 32)
    private String password;
}
