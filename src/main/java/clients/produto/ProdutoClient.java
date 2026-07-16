package clients.produto;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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
                .when()
                .post(PRODUTOS.getUrl());
    }

    public Response listarProdutos() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(PRODUTOS.getUrl());
    }

    public Response listarProdutosPorNome(String nome, Integer page, Integer size, String sort) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("nome", nome)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sort", sort)
                .when()
                .get(PRODUTOS.getUrl());
    }

    public Response listarProdutosPorCategoria(String categoria, Integer page, Integer size, String sort) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sort", sort)
                .when()
                .get(PRODUTOS.getUrl() + "/categoria/" + categoria);
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

    public Response atualizarProdutoSemToken(Integer produtoId, ProdutoRequest produto) {
        return given()
                .contentType(ContentType.JSON)
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

    public Response atualizarEstoqueSemToken(Integer produtoId, AtualizarEstoqueRequest estoque) {
        return given()
                .contentType(ContentType.JSON)
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

    public Response deletarProdutoSemAutenticacao(Integer produtoId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete(PRODUTOS.getUrl() + "/" + produtoId);
    }

    public Response listarProdutosComFiltros(String nome, String categoria, Double precoMin, Double precoMax, Integer page, Integer size, String sort) {
        RequestSpecification request = given()
                .contentType(ContentType.JSON);

        if (nome != null) {
            request.queryParam("nome", nome);
        }
        if (categoria != null) {
            request.queryParam("categoria", categoria);
        }
        if (precoMin != null) {
            request.queryParam("precoMin", precoMin);
        }
        if (precoMax != null) {
            request.queryParam("precoMax", precoMax);
        }
        if (page != null) {
            request.queryParam("page", page);
        }
        if (size != null) {
            request.queryParam("size", size);
        }
        if (sort != null) {
            request.queryParam("sort", sort);
        }

        return request
                .when()
                .get(PRODUTOS.getUrl());
    }
}
