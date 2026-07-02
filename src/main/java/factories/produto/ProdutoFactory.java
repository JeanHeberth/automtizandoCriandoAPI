package factories.produto;

import models.request.produto.ProdutoRequest;

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
        produto.setPreco(BigDecimal.valueOf(random.nextDouble() * 100).setScale(2, BigDecimal.ROUND_HALF_UP));
        produto.setEstoque(random.nextInt(100));
        produto.setCategoria(CATEGORIAS.get(random.nextInt(CATEGORIAS.size())));
        return produto;
    }

    public static ProdutoRequest produtoComEstoqueNegativo() {
        ProdutoRequest produto = produtoValido();
        produto.setEstoque(-1);
        return produto;
    }

    public static ProdutoRequest produtoComNomeVazio() {
        ProdutoRequest produto = produtoValido();
        produto.setNome("");
        return produto;
    }

    public static ProdutoRequest produtoComNomeCaractereMenorQueOPermitido() {
        ProdutoRequest produto = produtoValido();
        produto.setNome("a");
        return produto;
    }

    public static ProdutoRequest produtoComPrecoNegativo() {
        ProdutoRequest produto = produtoValido();
        produto.setPreco(BigDecimal.valueOf(-10));
        return produto;
    }

    public static ProdutoRequest produtoComDescricaoVazia() {
        ProdutoRequest produto = produtoValido();
        produto.setDescricao("");
        return produto;
    }

    public static ProdutoRequest produtoComCategoriaVazia() {
        ProdutoRequest produto = produtoValido();
        produto.setCategoria("");
        return produto;
    }

    public static ProdutoRequest produtoComCategoriaInvalida() {
        ProdutoRequest produto = produtoValido();
        produto.setCategoria("TESTE");
        return produto;
    }

}