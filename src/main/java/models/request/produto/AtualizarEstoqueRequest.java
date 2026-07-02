package models.request.produto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AtualizarEstoqueRequest {
    private Integer quantidade;
}
