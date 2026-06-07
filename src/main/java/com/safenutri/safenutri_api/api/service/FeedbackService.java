package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.Feedback;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.output.FeedbackOutput;
import com.safenutri.safenutri_api.repository.FeedbackRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserService userService; // Necessário para salvar o feedback do usuário

    public FeedbackService(FeedbackRepository feedbackRepository, UserService userService) {
        this.feedbackRepository = feedbackRepository;
        this.userService = userService;
    }

    // --- Método do Usuário (Restaurado) ---
    @Transactional
    public void salvar(String descricao, Long userId) {
        User user = userService.findById(userId);
        Feedback feedback = new Feedback(user, descricao);
        feedbackRepository.save(feedback);
    }

    // --- Métodos do Administrador (Novos) ---
    @Transactional(readOnly = true)
    public Page<FeedbackOutput> listarTodosPaginado(int page, int size) {
        return feedbackRepository.findAllFeedbacksPaginado(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public FeedbackOutput buscarDetalhePorId(Long id) {
        return feedbackRepository.findDetalheById(id);
    }
}