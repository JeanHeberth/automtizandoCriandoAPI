package pedido;

import base.BaseTest;
import constants.endpoints.Endpoint;
import factories.pedido.PedidoFactory;
import factories.produto.ProdutoFactory;
import models.request.pedido.PedidoRequest;
import models.request.produto.ProdutoRequest;
import org.testng.annotations.Test;

import static config.Configuration.getEndpoint;
import static constants.endpoints.Endpoint.*;
import static io.restassured.RestAssured.given;

public class PedidoTest extends BaseTest {

    @Test(description = "Deve criar um pedido com sucesso")
    public void criarPedidoComSucesso() {

        ProdutoRequest produto = ProdutoFactory.produtoValido();

        Integer produtoId =
                given()
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .body(produto)
                        .when()
                        .post(PRODUTOS.getUrl())
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        PedidoRequest pedido = PedidoFactory.pedidoValido(produtoId);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(pedido)
                .when()
                .post(PEDIDOS.getUrl())
                .then()
                .statusCode(201);
    }

}
