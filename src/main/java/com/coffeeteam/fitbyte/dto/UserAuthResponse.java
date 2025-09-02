package com.coffeeteam.fitbyte.dto;

import lombok.Data;


@Data
public class UserAuthResponse {
    private  String email;
    private String token;

    public UserAuthResponse(String email, String token) {
        this.email = email;
        this.token = token;
    }

}
