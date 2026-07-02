package models.request.pedido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoRequest {

    private Integer produtoId;
    private Integer quantidade;

}
