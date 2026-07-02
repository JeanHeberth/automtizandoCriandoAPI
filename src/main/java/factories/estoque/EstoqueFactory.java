package factories.estoque;

import models.request.produto.AtualizarEstoqueRequest;

public class EstoqueFactory {

    public static AtualizarEstoqueRequest estoqueValido() {
        return new AtualizarEstoqueRequest(50);
    }

    public static AtualizarEstoqueRequest estoqueZero() {
        return new AtualizarEstoqueRequest(0);
    }

    public static AtualizarEstoqueRequest estoqueNegativo() {
        return new AtualizarEstoqueRequest(-1);
    }
}
