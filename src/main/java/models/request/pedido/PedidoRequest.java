package models.request.pedido;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoRequest {

    private List<ItemPedidoRequest> itens;
}
