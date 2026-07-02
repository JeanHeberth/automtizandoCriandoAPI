package clients.produto;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.request.produto.AtualizarEstoqueRequest;
import models.request.produto.ProdutoRequest;

import static constants.endpoints.Endpoint.PRODUTOS;
import static factories.produto.ProdutoFactory.produtoValido;
import static io.restassured.RestAssured.given;

public class ProdutoClient {

    public Response criarProduto(String token, ProdutoRequest produto) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(produto)
                .when()
                .post(PRODUTOS.getUrl());
    }

    public Integer criarProdutoValido(String token) {
        return criarProdutoERetornarId(token, produtoValido());
    }

    public Response criarProdutoSemAutenticacao(ProdutoRequest produto) {
        return given()
                .contentType(ContentType.JSON)
                .body(produto)
                .body(produto)
                .when()
                .post(PRODUTOS.getUrl());
    }

    public Response listarProdutos() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(PRODUTOS.getUrl());
    }

    public Response buscarProdutoPorId(Integer produtoId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(PRODUTOS.getUrl() + "/" + produtoId);
    }

    public Integer criarProdutoERetornarId(String token, ProdutoRequest produto) {

        return criarProduto(token, produto)
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    public Response atualizarProduto(String token, Integer produtoId, ProdutoRequest produto) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(produto)
                .when()
                .put(PRODUTOS.getUrl() + "/" + produtoId);
    }

    public Response atualizarEstoque(String token, Integer produtoId, AtualizarEstoqueRequest estoque) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(estoque)
                .when()
                .patch(PRODUTOS.getUrl() + "/" + produtoId + "/estoque");
    }

    public Response deletarProduto(String token, Integer produtoId) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PRODUTOS.getUrl() + "/" + produtoId);
    }
}
