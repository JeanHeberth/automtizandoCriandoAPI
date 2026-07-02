package models.request.produto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoRequest {

    public String nome;
    public String descricao;
    public BigDecimal preco;
    public Integer estoque;
    public String categoria;
}
