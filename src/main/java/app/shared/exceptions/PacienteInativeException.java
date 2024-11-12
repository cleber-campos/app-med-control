package app.shared.exceptions;

public class PacienteInativeException extends RuntimeException {
    public PacienteInativeException(String message) {
        super(message);
    }
}