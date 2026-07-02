package produto;

import base.BaseTest;
import clients.ProdutoClient;
import factories.estoque.EstoqueFactory;
import factories.produto.ProdutoFactory;
import io.restassured.http.ContentType;

import io.restassured.response.Response;
import models.request.produto.AtualizarEstoqueRequest;
import models.request.produto.ProdutoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static config.Configuration.getEndpoint;
import static factories.estoque.EstoqueFactory.*;
import static factories.produto.ProdutoFactory.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ProdutoTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoTest.class);
    private final ProdutoClient produtoClient = new ProdutoClient();

    @Test(description = "Deve criar um produto com sucesso")
    public void criarProdutoComSucesso() {
        logger.info("Executando teste: criarProdutoComSucesso");

        ProdutoRequest produtoRequest = produtoValido();
        produtoClient.criarProduto(token, produtoRequest)
                .then()
                .statusCode(201);

        logger.info("Teste concluído: criarProdutoComSucesso");
    }

    @Test
    public void listarProdutoComSucesso() {

        logger.info("Executando teste: listarProdutoComSucesso");
        produtoClient.listarProdutos()
                .then()
                .statusCode(200);

        logger.info("Teste concluído: listarProdutoComSucesso");
    }

    @Test(description = "Deve retornar produto por id")
    public void buscarProdutoPorId() {

        logger.info("Executando teste: buscarProdutoPorId");

        Integer produtoId = produtoClient.criarProdutoERetornarId(token, produtoValido());
        produtoClient.buscarProdutoPorId(produtoId)
                .then()
                .statusCode(200);

        logger.info("Teste concluído: buscarProdutoPorId");
    }


    @Test(description = "Deve retornar 404 ao buscar produto por id inexistente")
    public void buscarProdutoPorIdInexistente() {

        logger.info("Executando teste: Buscar Produto Por Id Inexistente");

        produtoClient.buscarProdutoPorId(999999999)
                .then()
                .statusCode(404)
                .body("mensagem", containsString("Produto não encontrado"));

        logger.info("Teste concluído: Buscar Produto Por Id Inexistente");
    }

    @Test(description = "Deve retornar 401 ao tentar criar um produto sem autenticação")
    public void criarProdutoSemAutenticacao() {
        logger.info("Executando teste: criarProdutoSemAutenticacao");

        produtoClient.criarProdutoSemAutenticacao(produtoValido())
                .then()
                .statusCode(401)
                .body("error", containsString("Unauthorized"));

        logger.info("Teste concluído: criarProdutoSemAutenticacao");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto com nome vazio")
    public void criarProdutoComNomeVazio() {
        logger.info("Executando teste: Criar Produto Com Nome Vazio");

        produtoClient.criarProduto(token, produtoComNomeVazio())
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Nome é obrigatório"));

        logger.info("Teste concluído: Criar Produto Com Nome Vazio");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto com caractere menor que o permitido")
    public void criarProdutoComCaractereMenorQueOPermitido() {
        logger.info("Executando teste: Criar Produto Com Caractere Menor Que O Permitido");

        produtoClient.criarProduto(token, produtoComNomeCaractereMenorQueOPermitido())
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Nome deve ter entre 2 e 150 caracteres"));

        logger.info("Teste concluído: Criar Produto Com Caractere Menor Que O Permitido");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto sem descricao")
    public void criarProdutoSemDescricao() {

        logger.info("Executando teste: Criar Produto Sem Descricao");

        produtoClient.criarProduto(token, produtoComDescricaoVazia())
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Descrição é obrigatória"));


        logger.info("Teste concluído: Criar Produto Sem Descricao");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto com preço negativo")
    public void criarProdutoComPrecoNegativo() {

        logger.info("Executando teste: Criar Produto Com Preço Negativo");
        produtoClient.criarProduto(token, produtoComPrecoNegativo())
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Preço deve ser maior que zero"));

        logger.info("Teste concluído: Criar Produto Com Preço Negativo");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto com estoque negativo")
    public void criarProdutoComEstoqueNegativo() {

        logger.info("Executando teste: Criar Produto Com Estoque Negativo");

        produtoClient.criarProduto(token, produtoComEstoqueNegativo())
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Estoque não pode ser negativo"));

        logger.info("Teste concluído: Criar Produto Com Estoque Negativo");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto com categoria inválida")
    public void criarProdutoComCategoriaInvalida() {

        logger.info("Executando teste: Criar Produto Com Categoria Inválida");

        produtoClient.criarProduto(token, produtoComCategoriaInvalida())
                .then()
                .statusCode(400)
                .body("mensagem", containsString("Valor 'TESTE' invalido"));

        logger.info("Teste concluído: Criar Produto Com Categoria Inválida");
    }

    @Test(description = "Deve retornar 400 ao tentar criar um produto com categoria vazia")
    public void criarProdutoComCategoriaVazia() {

        logger.info("Executando teste: Criar Produto Com Categoria vazia");

        produtoClient.criarProduto(token, produtoComCategoriaVazia())
                .then()
                .statusCode(400)
                .body("mensagem", containsString("Valor '' invalido"));

        logger.info("Teste concluído: Criar Produto Com Categoria vazia");
    }

    @Test(description = "Deve retornar 200 ao atualizar um produto cadastrado")
    public void atualizarProduto() {

        logger.info("Executando teste: atualizar Produto");

        ProdutoRequest produtoRequest = produtoValido();

        Integer produtoId = produtoClient.criarProdutoERetornarId(token, produtoRequest);

        ProdutoRequest produtoRequestAtualizado = produtoValido();


        produtoClient.atualizarProduto(token, produtoId, produtoRequestAtualizado)
                .then()
                .statusCode(200)
                .body("nome", equalTo(produtoRequestAtualizado.getNome()))
                .body("descricao", equalTo(produtoRequestAtualizado.getDescricao()))
                .body("preco", equalTo(produtoRequestAtualizado.getPreco().floatValue()))
                .body("estoque", equalTo(produtoRequestAtualizado.getEstoque()))
                .body("categoria", equalTo(produtoRequestAtualizado.getCategoria()))
        ;

        logger.info("Teste concluído: Produto com ID: " + produtoId + " atualizado com sucesso");

    }

    @Test(description = "Deve retornar 200 ao atualizar o estoque de um produto cadastrado")
    public void atualizarEstoqueProduto() {

        logger.info("Executando teste: atualizarEstoqueProduto");

        Integer produtoId = produtoClient.criarProdutoValido(token);

        produtoClient
                .atualizarEstoque(token, produtoId, estoqueValido())
                .then()
                .statusCode(200)
                .body("estoque", equalTo(estoqueValido().getQuantidade()));

        logger.info("Teste concluído: Estoque do produto com ID: " + produtoId + " atualizado com sucesso");
    }
}
