package com.safenutri.safenutri_api.api.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.UserRestricao;
import com.safenutri.safenutri_api.repository.UserRestricaoRepository;
import jakarta.transaction.Transactional; 
@Service
@RequiredArgsConstructor
public class UserRestricaoService {
    private final UserRestricaoRepository userRestricaoRepository;

    public void saveAll(List<UserRestricao> userRestricoes) {
        userRestricaoRepository.saveAll(userRestricoes);
    }
    @Transactional
    public void deleteByUserId(Long userId) {
        userRestricaoRepository.deleteByUserId(userId);
    }
}
