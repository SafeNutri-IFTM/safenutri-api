package com.safenutri.safenutri_api.api.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safenutri.safenutri_api.repository.FeedbackRepository;
import com.safenutri.safenutri_api.model.entity.Feedback;
import com.safenutri.safenutri_api.model.entity.User;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserService userService;

    @Transactional
    public void salvar(String descricao, Long userId) {
        User user = userService.findById(userId);
        Feedback feedback = new Feedback(user, descricao);
        feedbackRepository.save(feedback);
    }
}