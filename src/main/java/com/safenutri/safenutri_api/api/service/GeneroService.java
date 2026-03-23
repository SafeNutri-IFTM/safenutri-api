package com.safenutri.safenutri_api.api.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.safenutri.safenutri_api.model.entity.Genero;
import com.safenutri.safenutri_api.repository.GeneroRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneroService {

    private final GeneroRepository generoRepository;

    public Genero findById(Long id) {
        return generoRepository.findById(id).orElseThrow(() -> new RuntimeException("Genero não encontrado"));
    }

    public List<Genero> getAll() {
        return generoRepository.findAll();
    }
}
