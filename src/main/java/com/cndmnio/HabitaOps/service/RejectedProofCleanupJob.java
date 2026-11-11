package com.cndmnio.HabitaOps.service;

import com.cndmnio.HabitaOps.config.FileStorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

/**
 * BR-PY-017: borra los comprobantes rechazados que superan los días de retención.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RejectedProofCleanupJob {

    private final FileStorageProperties props;

    @Scheduled(cron = "${app.upload.cleanup-cron:0 0 3 * * *}")
    public void deleteExpiredRejected() {
        Path dir = Path.of(props.getRejectedDir());
        if (!Files.isDirectory(dir)) {
            return;
        }
        Instant cutoff = Instant.now().minus(props.getRejectedRetentionDays(), ChronoUnit.DAYS);
        try (Stream<Path> files = Files.list(dir)) {
            files.forEach(path -> deleteIfExpired(path, cutoff));
        } catch (IOException e) {
            log.error("No se pudo listar {}: {}", dir, e.getMessage());
        }
    }

    private void deleteIfExpired(Path path, Instant cutoff) {
        try {
            if (!Files.isRegularFile(path)) {
                return;
            }
            if (Files.getLastModifiedTime(path).toInstant().isBefore(cutoff)) {
                Files.deleteIfExists(path);
                log.info("Comprobante rechazado expirado eliminado: {}", path);
            }
        } catch (IOException e) {
            log.warn("No se pudo borrar {}: {}", path, e.getMessage());
        }
    }
}
