package com.safenutri.safenutri_api.model.input;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginInput {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
