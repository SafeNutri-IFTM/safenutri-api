package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.model.entity.Role;
import com.safenutri.safenutri_api.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService service;

    @GetMapping
    public List<Role> listar() {
        return service.listarTodas();
    }

    @PostMapping
    public Role criar(@RequestBody Role role) {
        return service.salvar(role);
    }
}
