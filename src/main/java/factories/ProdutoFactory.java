package factories;

import models.request.ProdutoRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ProdutoFactory {

    private static final Random random = new Random();

    private static final List<String> CATEGORIAS = List.of(
            "ELETRONICO",
            "VESTUARIO",
            "ALIMENTACAO",
            "LIVRO",
            "ESPORTE",
            "CASA_E_JARDIM",
            "OUTRO"
    );

    public static ProdutoRequest produtoValido() {
        ProdutoRequest produto = new ProdutoRequest();
        produto.setNome("Produto " + UUID.randomUUID());
        produto.setDescricao("Descrição do produto " + UUID.randomUUID());
        produto.setPreco(Double.valueOf(random.nextDouble() * 100));
        produto.setEstoque(random.nextInt(100));
        produto.setCategoria(CATEGORIAS.get(random.nextInt(CATEGORIAS.size())));
        return produto;
    }
}