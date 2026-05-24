package com.safenutri.safenutri_api.api.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.safenutri.safenutri_api.repository.FeedbackRepository;
import com.safenutri.safenutri_api.repository.UserRepository;
import com.safenutri.safenutri_api.model.entity.Feedback;
import com.safenutri.safenutri_api.model.entity.User;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public void salvar(String descricao, String emailUsuario) {
        // Busca o usuário logado pelo e-mail (extraído do token JWT via Principal)
        User user = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Cria a entidade Feedback usando o construtor que você já tem definido
        Feedback feedback = new Feedback(user, descricao);
        
        feedbackRepository.save(feedback);
    }
}