package produto;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import base.BaseTest;
import clients.produto.ProdutoClient;
import static constants.Ids.ID_INEXISTENTE;
import static factories.estoque.EstoqueFactory.estoqueValido;
import static factories.estoque.EstoqueFactory.estoqueNegativo;
import static factories.produto.ProdutoFactory.produtoComCategoriaInvalida;
import static factories.produto.ProdutoFactory.produtoComCategoriaVazia;
import static factories.produto.ProdutoFactory.produtoComDescricaoVazia;
import static factories.produto.ProdutoFactory.produtoComEstoqueNegativo;
import static factories.produto.ProdutoFactory.produtoComNomeCaractereMenorQueOPermitido;
import static factories.produto.ProdutoFactory.produtoComNomeVazio;
import static factories.produto.ProdutoFactory.produtoComPrecoNegativo;
import static factories.produto.ProdutoFactory.produtoValidoComCategoria;
import static factories.produto.ProdutoFactory.produtoValido;
import models.request.produto.ProdutoRequest;

@Test(groups = "produtos")
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

    @Test(description = "Deve falhar ao criar um produto com nome duplicado")
    public void criarProdutoComNomeDuplicado() {
        logger.info("Executando teste: criarProdutoComNomeDuplicado");

        ProdutoRequest produto = produtoValido();
        produtoClient.criarProduto(token, produto)
                .then()
                .statusCode(201);

        produtoClient.criarProduto(token, produto)
                .then()
                .statusCode(409);

        logger.info("Falha ao criar produto com nome duplicado: " + produto.getNome());
    }

    @Test(description = "Deve listar produtos com sucesso")
    public void listarProdutoComSucesso() {

        logger.info("Executando teste: listarProdutoComSucesso");
        produtoClient.listarProdutos()
                .then()
                .statusCode(200);

        logger.info("Teste concluído: listarProdutoComSucesso");
    }

    @Test(description = "Deve listar produtos por nome com sucesso")
    public void listarProdutosPorNomeComSucesso() {
        logger.info("Executando teste: listarProdutosPorNomeComSucesso");

        ProdutoRequest produto = produtoValido();
        produtoClient.criarProdutoERetornarId(token, produto);

        produtoClient.listarProdutosPorNome(produto.getNome(), 0, 5, "nome,asc")
                .then()
                .statusCode(200)
                .body("content.nome", hasItem(produto.getNome()));

        logger.info("Teste concluído: listarProdutosPorNomeComSucesso");
    }

    @Test(description = "Deve retornar 200 com lista vazia ao buscar produto por nome inexistente")
    public void listarProdutosPorNomeSemResultado() {
        logger.info("Executando teste: listarProdutosPorNomeSemResultado");

        produtoClient.listarProdutosPorNome("nome-inexistente-" + System.currentTimeMillis(), 0, 5, "nome,asc")
                .then()
                .statusCode(200)
                .body("content", hasSize(0));

        logger.info("Teste concluído: listarProdutosPorNomeSemResultado");
    }

    @Test(description = "Deve listar produtos por categoria com sucesso")
    public void listarProdutosPorCategoriaComSucesso() {
        logger.info("Executando teste: listarProdutosPorCategoriaComSucesso");

        ProdutoRequest produto = produtoValidoComCategoria("ELETRONICO");
        produtoClient.criarProdutoERetornarId(token, produto);

        produtoClient.listarProdutosPorCategoria("ELETRONICO", 0, 100, "nome,asc")
                .then()
                .statusCode(200)
                .body("content.nome", hasItem(produto.getNome()));

        logger.info("Teste concluído: listarProdutosPorCategoriaComSucesso");
    }

    @Test(description = "Deve retornar 400 ao listar produtos com filtro de categoria inválido")
    public void listarProdutosComCategoriaInvalida() {
        logger.info("Executando teste: listarProdutosComCategoriaInvalida");

        produtoClient.listarProdutosComFiltros(null, "TESTE", null, null, 0, 5, "nome,asc")
                .then()
                .statusCode(400);

        logger.info("Teste concluído: listarProdutosComCategoriaInvalida");
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

        produtoClient.buscarProdutoPorId(ID_INEXISTENTE)
                .then()
                .statusCode(404)
                .body("mensagem", containsString("Produto não encontrado com id: " + ID_INEXISTENTE));

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
                .body("categoria", equalTo(produtoRequestAtualizado.getCategoria()));

        logger.info("Teste concluído: Produto com ID: " + produtoId + " atualizado com sucesso");

    }

    @Test(description = "Deve retornar 400 ao atualizar um produto com nome vazio")
    public void atualizarProdutoComNomeVazio() {
        logger.info("Executando teste: atualizarProdutoComNomeVazio");

        Integer produtoId = produtoClient.criarProdutoERetornarId(token, produtoValido());

        produtoClient.atualizarProduto(token, produtoId, produtoComNomeVazio())
                .then()
                .statusCode(400);

        logger.info("Teste concluído: atualizarProdutoComNomeVazio");
    }

    @Test(description = "Deve retornar 404 ao atualizar um produto inexistente")
    public void atualizarProdutoInexistente() {
        logger.info("Executando teste: atualizarProdutoInexistente");

        produtoClient.atualizarProduto(token, ID_INEXISTENTE, produtoValido())
                .then()
                .statusCode(404);

        logger.info("Teste concluído: atualizarProdutoInexistente");
    }

    @Test(description = "Deve retornar 401 ao atualizar um produto sem autenticação")
    public void atualizarProdutoSemAutenticacao() {
        logger.info("Executando teste: atualizarProdutoSemAutenticacao");

        Integer produtoId = produtoClient.criarProdutoERetornarId(token, produtoValido());

        produtoClient.atualizarProdutoSemToken(produtoId, produtoValido())
                .then()
                .statusCode(401);

        logger.info("Teste concluído: atualizarProdutoSemAutenticacao");
    }

    @Test(description = "Deve retornar 409 ao atualizar um produto com nome duplicado")
    public void atualizarProdutoComNomeDuplicado() {
        logger.info("Executando teste: atualizarProdutoComNomeDuplicado");

        ProdutoRequest produtoOriginal = produtoValido();
        ProdutoRequest produtoDuplicado = produtoValido();

        Integer produtoId = produtoClient.criarProdutoERetornarId(token, produtoOriginal);
        produtoClient.criarProdutoERetornarId(token, produtoDuplicado);

        produtoOriginal.setNome(produtoDuplicado.getNome());

        produtoClient.atualizarProduto(token, produtoId, produtoOriginal)
                .then()
                .statusCode(409);

        logger.info("Teste concluído: atualizarProdutoComNomeDuplicado");
    }

    @Test(description = "Deve retornar 200 ao atualizar o estoque de um produto cadastrado")
    public void atualizarEstoqueProduto() {

        logger.info("Executando teste: atualizarEstoqueProduto");

        Integer produtoId = produtoClient.criarProdutoValido(token);

        produtoClient
                .atualizarEstoque(token, produtoId, estoqueValido())
                .then()
                .statusCode(200)
                .body("estoque", equalTo(estoqueValido().getEstoque()));

        logger.info("Teste concluído: Estoque do produto com ID: " + produtoId + " atualizado com sucesso");
    }

    @Test(description = "Deve retornar 400 ao atualizar o estoque com valor negativo")
    public void atualizarEstoqueProdutoComValorNegativo() {
        logger.info("Executando teste: atualizarEstoqueProdutoComValorNegativo");

        Integer produtoId = produtoClient.criarProdutoERetornarId(token, produtoValido());

        produtoClient.atualizarEstoque(token, produtoId, estoqueNegativo())
                .then()
                .statusCode(400);

        logger.info("Teste concluído: atualizarEstoqueProdutoComValorNegativo");
    }

    @Test(description = "Deve retornar 404 ao atualizar o estoque de um produto inexistente")
    public void atualizarEstoqueProdutoInexistente() {
        logger.info("Executando teste: atualizarEstoqueProdutoInexistente");

        produtoClient.atualizarEstoque(token, ID_INEXISTENTE, estoqueValido())
                .then()
                .statusCode(404);

        logger.info("Teste concluído: atualizarEstoqueProdutoInexistente");
    }

    @Test(description = "Deve retornar 401 ao atualizar o estoque sem autenticação")
    public void atualizarEstoqueProdutoSemAutenticacao() {
        logger.info("Executando teste: atualizarEstoqueProdutoSemAutenticacao");

        Integer produtoId = produtoClient.criarProdutoERetornarId(token, produtoValido());

        produtoClient.atualizarEstoqueSemToken(produtoId, estoqueValido())
                .then()
                .statusCode(401);

        logger.info("Teste concluído: atualizarEstoqueProdutoSemAutenticacao");
    }

    @Test(description = "Deve retornar 204 ao deletar um produto cadastrado")
    public void deletarProdutoComSucesso() {
        logger.info("Executando teste: deletarProdutoComSucesso");

        Integer produtoId = produtoClient.criarProdutoValido(token);

        produtoClient.deletarProduto(token, produtoId)
                .then()
                .statusCode(204);

        logger.info("Teste concluído: deletarProdutoComSucesso");
    }

    @Test(description = "Deve retornar 404 ao deletar um produto inexistente")
    public void deletarProdutoInexistente() {
        logger.info("Executando teste: deletarProdutoInexistente");

        produtoClient.deletarProduto(token, ID_INEXISTENTE)
                .then()
                .statusCode(404);

        logger.info("Teste concluído: deletarProdutoInexistente");
    }

    @Test(description = "Deve retornar 401 ao deletar um produto sem autenticação")
    public void deletarProdutoSemAutenticacao() {
        logger.info("Executando teste: deletarProdutoSemAutenticacao");

        Integer produtoId = produtoClient.criarProdutoValido(token);

        produtoClient.deletarProdutoSemAutenticacao(produtoId)
                .then()
                .statusCode(401)
                .body("error", containsString("Unauthorized"));

        logger.info("Teste concluído: deletarProdutoSemAutenticacao");
    }
}
