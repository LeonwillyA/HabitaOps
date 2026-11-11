package com.cndmnio.HabitaOps.service;

import com.cndmnio.HabitaOps.config.FileStorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Movimiento de los comprobantes entre carpetas (BR-PY-013..016).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private final FileStorageProperties props;

    /**
     * BR-PY-014: mueve el comprobante de tmp/ a approved/.
     * Devuelve la nueva ruta, o {@code null} si el movimiento falla.
     */
    public String moveToApproved(String proofFileUrl) {
        return move(proofFileUrl, props.getApprovedDir());
    }

    /**
     * BR-PY-015: mueve el comprobante de tmp/ a rejected/.
     * Devuelve la nueva ruta, o {@code null} si el movimiento falla.
     */
    public String moveToRejected(String proofFileUrl) {
        return move(proofFileUrl, props.getRejectedDir());
    }

    /**
     * BR-PY-016: si el movimiento falla, el archivo queda en tmp, se registra el error y
     * se devuelve {@code null} para que el caller NO actualice proofFileUrl.
     * El reintento es llamar de nuevo: proofFileUrl sigue apuntando a tmp.
     */
    private String move(String proofFileUrl, String targetDir) {
        if (proofFileUrl == null || proofFileUrl.isBlank()) {
            log.warn("proofFileUrl vacío; no se puede mover el comprobante.");
            return null;
        }
        Path source = Path.of(proofFileUrl);
        try {
            Path target = Path.of(targetDir).resolve(source.getFileName()).normalize();
            Files.createDirectories(target.getParent());
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            return target.toString();
        } catch (IOException e) {
            log.error("No se pudo mover {} a {}: {}. El archivo queda en tmp (BR-PY-016).",
                    source, targetDir, e.getMessage());
            return null;
        }
    }
}
