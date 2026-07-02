package produto;

import auth.AuthService;
import base.BaseTest;
import io.restassured.http.ContentType;

import models.request.ProdutoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static config.Configuration.getEndpoint;
import static factories.ProdutoFactory.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ProdutoTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoTest.class);

    @Test(description = "Deve criar um produto com sucesso")
    public void criarProdutoComSucesso() {
        logger.info("Executando teste: criarProdutoComSucesso");
        String token = AuthService.getToken();

        ProdutoRequest produto = produtoValido();
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(produto)
                .when()
                .post(getEndpoint("produtos"))
                .then()
                .statusCode(201);
        logger.info("Teste concluído: criarProdutoComSucesso");
    }

    @Test
    public void listarProdutoComSucesso() {
        logger.info("Executando teste: listarProdutoComSucesso");

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(getEndpoint("produtos"))
                .then()
                .statusCode(200);

        logger.info("Teste concluído: listarProdutoComSucesso");
    }

    @Test(description = "Deve retornar 401 ao tentar criar um produto sem autenticação")
    public void criarProdutoSemAutenticacao() {
        logger.info("Executando teste: criarProdutoSemAutenticacao");
        given()
                .contentType(ContentType.JSON)
                .body(produtoValido())
                .when()
                .post(getEndpoint("produtos"))
                .then()
                .statusCode(401);

        logger.info("Teste concluído: criarProdutoSemAutenticacao");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto com nome vazio")
    public void criarProdutoComNomeVazio() {
        logger.info("Executando teste: Criar Produto Com Nome Vazio");

        String token = AuthService.getToken();
        ProdutoRequest produto = produtoValido();
        produto.setNome("");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(produto)
                .when()
                .post(getEndpoint("produtos"))
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Nome é obrigatório"));

        logger.info("Teste concluído: Criar Produto Com Nome Vazio");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto com caractere menor que o permitido")
    public void criarProdutoComCaractereMenorQueOPermitido() {
        logger.info("Executando teste: Criar Produto Com Caractere Menor Que O Permitido");

        String token = AuthService.getToken();
        ProdutoRequest produto = produtoValido();
        produto.setNome("a");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(produto)
                .when()
                .post(getEndpoint("produtos"))
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Nome deve ter entre 2 e 150 caracteres"));

        logger.info("Teste concluído: Criar Produto Com Caractere Menor Que O Permitido");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto sem descricao")
    public void criarProdutoSemDescricao() {

        logger.info("Executando teste: Criar Produto Sem Descricao");

        String token = AuthService.getToken();
        ProdutoRequest produto = produtoValido();
        produto.setDescricao("");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(produto)
                .when()
                .post(getEndpoint("produtos"))
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Descrição é obrigatória"));

        logger.info("Teste concluído: Criar Produto Sem Descricao");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto com preço negativo")
    public void criarProdutoComPrecoNegativo() {
        logger.info("Executando teste: Criar Produto Com Preço Negativo");
        String token = AuthService.getToken();
        ProdutoRequest produto = produtoValido();
        produto.setPreco(-10.0);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(produto)
                .when()
                .post(getEndpoint("produtos"))
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Preço deve ser maior que zero"));

        logger.info("Teste concluído: Criar Produto Com Preço Negativo");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto com estoque negativo")
    public void criarProdutoComEstoqueNegativo() {
        logger.info("Executando teste: Criar Produto Com Estoque Negativo");
        String token = AuthService.getToken();
        ProdutoRequest produto = produtoValido();
        produto.setEstoque(-5);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(produto)
                .when()
                .post(getEndpoint("produtos"))
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Estoque não pode ser negativo"));

        logger.info("Teste concluído: Criar Produto Com Estoque Negativo");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto com categoria inválida")
    public void criarProdutoComCategoriaInvalida() {
        logger.info("Executando teste: Criar Produto Com Categoria Inválida");

        String token = AuthService.getToken();
        ProdutoRequest produto = produtoValido();

        produto.setCategoria("TESTE");
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(produto)
                .when()
                .post(getEndpoint("produtos"))
                .then()
                .statusCode(400)
                .body("mensagem", containsString("Valor 'TESTE' invalido."));

        logger.info("Teste concluído: Criar Produto Com Categoria Inválida");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto com categoria vazia")
    public void criarProdutoComCategoriaVazia() {
        logger.info("Executando teste: Criar Produto Com Categoria vazia");

        String token = AuthService.getToken();
        ProdutoRequest produto = produtoValido();

        produto.setCategoria("");
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(produto)
                .when()
                .post(getEndpoint("produtos"))
                .then()
                .statusCode(400)
                .body("mensagem", containsString("Valor '' invalido."));

        logger.info("Teste concluído: Criar Produto Com Categoria vazia");
    }
}
