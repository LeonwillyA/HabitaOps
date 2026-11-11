package com.cndmnio.HabitaOps.service;

import org.springframework.stereotype.Service;

/**
 * Registro y validación de pagos.
 */
@Service
public class PaymentService {

    /**
     * BR-PY-009: valida el comprobante solo por extensión (JPG/PNG) y por tamaño (no vacío).
     *
     * <p>La legibilidad NO se valida acá: sigue siendo revisión manual del administrador
     * (BR-PY-010 / BR-PY-011). Este método es el chequeo técnico previo a guardarlo.</p>
     *
     * @param filename nombre original del archivo (antes de renombrarlo a UUID)
     * @param size     tamaño en bytes del archivo
     */
    public void validateProof(String filename, long size) {
        String extension = extensionOf(filename);
        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png"))  {
            throw new IllegalArgumentException(
                    "Formato de comprobante no soportado: '" + extension
                            + "'. Solo se aceptan JPG y PNG (BR-PY-009).");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("El comprobante está vacío.");
        }
    }

    private String extensionOf(String filename) {
        if (filename == null || filename.isBlank()) {
            return "";
        }
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) {
            return "";
        }
        return filename.substring(dot + 1).toLowerCase();
    }
}
