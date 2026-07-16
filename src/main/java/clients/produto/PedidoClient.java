package clients.produto;

import constants.endpoints.Endpoint;
import static factories.pedido.PedidoFactory.pedidoValido;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import models.request.pedido.PedidoRequest;
import models.request.pedido.StatusPedidoRequest;

public class PedidoClient {

    public Response criarPedido(String token, PedidoRequest pedido) {
        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(pedido)
                .when()
                .post(Endpoint.PEDIDOS.getUrl());
    }

    public Response criarPedidoSemToken(PedidoRequest pedido) {
        return given()
                .contentType("application/json")
                .body(pedido)
                .when()
                .post(Endpoint.PEDIDOS.getUrl());
    }

    public Response buscarPedidoPorId(String token, Integer pedidoId) {
        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(Endpoint.PEDIDOS.byId(pedidoId));
    }

    public Response buscarPedidoPorIdSemToken(Integer pedidoId) {
        return given()
                .contentType("application/json")
                .when()
                .get(Endpoint.PEDIDOS.byId(pedidoId));
    }

    public Response buscarPedidoPorIdInvalido(String token, String pedidoId) {
        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(Endpoint.PEDIDOS.getUrl() + "/" + pedidoId);
    }

    public Response listarPedidos(String token) {
        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(Endpoint.PEDIDOS.getUrl());
    }

    public Response listarPedidosComPaginacao(String token, int page, int size) {
        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(Endpoint.PEDIDOS.getUrl());
    }

    public Response listarPedidosComStatus(String token, String status) {
        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .queryParam("status", status)
                .when()
                .get(Endpoint.PEDIDOS.getUrl());
    }

    public Response listarPedidosComStatusSemToken(String status) {
        return given()
                .contentType("application/json")
                .queryParam("status", status)
                .when()
                .get(Endpoint.PEDIDOS.getUrl());
    }

    public Response listarPedidosSemToken() {
        return given()
                .contentType("application/json")
                .when()
                .get(Endpoint.PEDIDOS.getUrl());
    }

    public Integer criarPedidoERetornarId(String token, PedidoRequest pedido) {
        return criarPedido(token, pedido)
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    public Integer criarPedidoValido(String token, Integer produtoId) {
        return criarPedidoERetornarId(token, pedidoValido(produtoId));
    }

    public Response atualizarPedido(String token, Integer pedidoId, PedidoRequest pedido) {
        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(pedido)
                .when()
                .put(Endpoint.PEDIDOS.byId(pedidoId));
    }

    public Response atualizarPedidoSemToken(Integer pedidoId, PedidoRequest pedido) {
        return given()
                .contentType("application/json")
                .body(pedido)
                .when()
                .put(Endpoint.PEDIDOS.byId(pedidoId));
    }

    public Response cancelarPedido(String token, Integer pedidoId) {
        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(Endpoint.PEDIDOS.byId(pedidoId));
    }

    public Response cancelarPedidoSemToken(Integer pedidoId) {
        return given()
                .contentType("application/json")
                .when()
                .delete(Endpoint.PEDIDOS.byId(pedidoId));
    }

    public Response transicionarStatus(String token, Integer pedidoId, String status) {
        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(new StatusPedidoRequest(status))
                .when()
                .patch(Endpoint.PEDIDOS.getUrl() + "/" + pedidoId + "/status");
    }

    public Response transicionarStatusSemToken(Integer pedidoId, String status) {
        return given()
                .contentType("application/json")
                .body(new StatusPedidoRequest(status))
                .when()
                .patch(Endpoint.PEDIDOS.getUrl() + "/" + pedidoId + "/status");
    }
}
