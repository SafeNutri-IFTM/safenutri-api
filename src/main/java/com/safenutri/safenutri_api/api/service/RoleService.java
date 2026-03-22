package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.Role;
import com.safenutri.safenutri_api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository repository;

    public List<Role> listarTodas() {
        return repository.findAll();
    }

    public Role salvar(Role role) {
        return repository.save(role);
    }
}
