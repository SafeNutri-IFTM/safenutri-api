package com.safenutri.safenutri_api.core.utils;

import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

public class GerarChave {
    public static void main(String[] args) {
        String chave = Encoders.BASE64.encode(
                Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
        System.out.println(chave);
    }
}
