package com.safenutri.safenutri_api.api.service;

import java.util.List;

import com.safenutri.safenutri_api.model.output.RestricaoOutput;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safenutri.safenutri_api.model.entity.UserRestricao;
import com.safenutri.safenutri_api.repository.UserRestricaoRepository;

@Service
@RequiredArgsConstructor
public class UserRestricaoService {
    private final UserRestricaoRepository userRestricaoRepository;

    @Transactional
    public void saveAll(List<UserRestricao> userRestricoes) {
        userRestricaoRepository.saveAll(userRestricoes);
    }

    @Transactional
    public void deleteByUserId(Long id) {
        userRestricaoRepository.deleteByUserId(id);
    }

    public List<Long> findAllByUserId(Long userId) {
        return userRestricaoRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<RestricaoOutput> findRestricoesByUserId(Long userId) {
        return userRestricaoRepository.findRestricoesByUserId(userId);
    }
}
