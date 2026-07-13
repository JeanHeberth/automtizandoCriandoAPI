package factories.pedido;

import java.util.Collections;
import java.util.List;

import models.request.pedido.ItemPedidoRequest;
import models.request.pedido.PedidoRequest;


public class PedidoFactory {

    public static PedidoRequest pedidoValido(Integer produtoId) {
        ItemPedidoRequest item = new ItemPedidoRequest();
        item.setProdutoId(produtoId);
        item.setQuantidade(2);

        PedidoRequest pedido = new PedidoRequest();
        pedido.setItens(List.of(item));
        return pedido;
    }

    public static PedidoRequest pedidoComQuantidadeVazia(Integer produtoId) {
        ItemPedidoRequest item = new ItemPedidoRequest();
        item.setProdutoId(produtoId);
        item.setQuantidade(0);

        PedidoRequest pedido = new PedidoRequest();
        pedido.setItens(List.of(item));
        return pedido;
    }

    public static PedidoRequest pedidoComQuantidadeNegativa(Integer produtoId) {
        ItemPedidoRequest item = new ItemPedidoRequest();
        item.setProdutoId(produtoId);
        item.setQuantidade(-1);

        PedidoRequest pedido = new PedidoRequest();
        pedido.setItens(List.of(item));
        return pedido;
    }

    public static PedidoRequest pedidoComListaDeItensVazia() {
        PedidoRequest pedido = new PedidoRequest();
        pedido.setItens(Collections.emptyList());
        return pedido;
    }

    public static PedidoRequest pedidoComProdutoIdInexistente() {
        ItemPedidoRequest item = new ItemPedidoRequest();
        item.setProdutoId(999999);
        item.setQuantidade(1);

        PedidoRequest pedido = new PedidoRequest();
        pedido.setItens(List.of(item));
        return pedido;
    }
}
