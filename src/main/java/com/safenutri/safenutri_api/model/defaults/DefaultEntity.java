package com.safenutri.safenutri_api.model.defaults;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class DefaultEntity {
    @NotNull
    @Column(name = "activated")
    private Boolean activated;

    @NotNull
    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "updated")
    private LocalDateTime updated;

    @PrePersist
    public void onCreate() {
        this.activated = Boolean.TRUE;
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updated = LocalDateTime.now();
    }
}
