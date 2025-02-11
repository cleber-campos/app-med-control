package app.shared.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TokenDTO(
        String token,
        LocalDateTime dataExpiracao) {
}
