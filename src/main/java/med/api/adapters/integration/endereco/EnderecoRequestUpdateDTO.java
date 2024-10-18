package med.api.adapters.integration.endereco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EnderecoRequestUpdateDTO(
        @NotBlank
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String uf,
        @Pattern(regexp = "\\d{8}")
        String cep) {
}
