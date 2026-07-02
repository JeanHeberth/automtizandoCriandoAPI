package models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoRequest {

    public String nome;
    public String descricao;
    public Double preco;
    public Integer estoque;
    public String categoria;
}
