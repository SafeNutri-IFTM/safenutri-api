package com.safenutri.safenutri_api.model.projection;

import java.time.LocalDateTime;

public interface QtdUserByMesProjection {
    LocalDateTime getMes();
    Long getQntd();
}
