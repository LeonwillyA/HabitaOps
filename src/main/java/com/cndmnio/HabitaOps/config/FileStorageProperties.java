package com.cndmnio.HabitaOps.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Carpetas y retención de los comprobantes de pago (BR-PY-013..017).
 */
@ConfigurationProperties(prefix = "app.upload")
@Getter
@Setter
public class FileStorageProperties {

    /** Carpeta temporal al registrar (BR-PY-013). */
    private String tmpDir = "uploads/tmp";

    /** Carpeta de comprobantes aprobados (BR-PY-014). */
    private String approvedDir = "uploads/approved";

    /** Carpeta de comprobantes rechazados (BR-PY-015). */
    private String rejectedDir = "uploads/rejected";

    /** Años de retención de los aprobados (BR-PY-017). */
    private int approvedRetentionYears = 20;

    /** Días de retención de los rechazados antes de borrarlos (BR-PY-017). */
    private int rejectedRetentionDays = 30;
}
