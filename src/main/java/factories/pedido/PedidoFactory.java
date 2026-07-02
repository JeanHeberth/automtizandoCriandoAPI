package factories.pedido;

import models.request.pedido.ItemPedidoRequest;
import models.request.pedido.PedidoRequest;

import java.util.List;

public class PedidoFactory {

    public static PedidoRequest pedidoValido(Integer produtoId) {

        ItemPedidoRequest item = new ItemPedidoRequest();

        item.setProdutoId(produtoId);
        item.setQuantidade(2);

        PedidoRequest pedido = new PedidoRequest();

        pedido.setItens(List.of(item));

        return pedido;
    }

}
