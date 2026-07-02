package pedido;

import base.BaseTest;
import factories.pedido.PedidoFactory;
import factories.produto.ProdutoFactory;
import models.request.pedido.PedidoRequest;
import models.request.produto.ProdutoRequest;
import org.testng.annotations.Test;

import static config.Configuration.getEndpoint;
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
                        .post(getEndpoint("produtos"))
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
                .post(getEndpoint("pedidos"))
                .then()
                .statusCode(201);
    }

}
