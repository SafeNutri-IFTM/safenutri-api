package com.safenutri.safenutri_api.model.input;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NutricionistaInput {
    private UserInput userInput;
    private String CRN;
}
