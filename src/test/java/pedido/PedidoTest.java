package pedido;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import base.BaseTest;
import clients.produto.PedidoClient;
import clients.produto.ProdutoClient;
import static constants.Ids.ID_INEXISTENTE;
import static factories.pedido.PedidoFactory.pedidoComListaDeItensVazia;
import static factories.pedido.PedidoFactory.pedidoComProdutoIdInexistente;
import static factories.pedido.PedidoFactory.pedidoComQuantidadeNegativa;
import static factories.pedido.PedidoFactory.pedidoComQuantidadeZero;
import static factories.pedido.PedidoFactory.pedidoValido;
import models.request.pedido.PedidoRequest;

@Test(groups = "pedidos")
public class PedidoTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(PedidoTest.class);
    private final PedidoClient pedidoClient = new PedidoClient();
    private final ProdutoClient produtoClient = new ProdutoClient();

    // =========================================================
    // POST /pedidos
    // =========================================================

    @Test(description = "Deve criar um pedido com sucesso - 201")
    public void criarPedidoComSucesso() {
        logger.info("Executando teste: criarPedidoComSucesso");

        Integer produtoId = produtoClient.criarProdutoValido(token);
        PedidoRequest pedidoRequest = pedidoValido(produtoId);
        Integer pedidoId = pedidoClient.criarPedidoERetornarId(token, pedidoRequest);

        pedidoClient.buscarPedidoPorId(token, pedidoId)
                .then()
                .statusCode(200);

        logger.info("Teste concluído: criarPedidoComSucesso");
    }

    @Test(description = "Deve retornar 400 ao criar pedido com lista de itens vazia")
    public void criarPedidoComListaDeItensVazia() {
        logger.info("Executando teste: criarPedidoComListaDeItensVazia");

        pedidoClient.criarPedido(token, pedidoComListaDeItensVazia())
                .then()
                .statusCode(400);

        logger.info("Teste concluído: criarPedidoComListaDeItensVazia");
    }

    @Test(description = "Deve retornar 400 ao criar pedido com quantidade zero")
    public void criarPedidoComQuantidadeZero() {
        logger.info("Executando teste: criarPedidoComQuantidadeZero");

        Integer produtoId = produtoClient.criarProdutoValido(token);

        pedidoClient.criarPedido(token, pedidoComQuantidadeZero(produtoId))
                .then()
                .statusCode(400);

        logger.info("Teste concluído: criarPedidoComQuantidadeZero");
    }

    @Test(description = "Deve retornar 400 ao criar pedido com quantidade negativa")
    public void criarPedidoComQuantidadeNegativa() {
        logger.info("Executando teste: criarPedidoComQuantidadeNegativa");

        Integer produtoId = produtoClient.criarProdutoValido(token);

        pedidoClient.criarPedido(token, pedidoComQuantidadeNegativa(produtoId))
                .then()
                .statusCode(400);

        logger.info("Teste concluído: criarPedidoComQuantidadeNegativa");
    }

    @Test(description = "Deve retornar 404 ao criar pedido com produtoId inexistente")
    public void criarPedidoComProdutoIdInexistente() {
        logger.info("Executando teste: criarPedidoComProdutoIdInexistente");

        pedidoClient.criarPedido(token, pedidoComProdutoIdInexistente())
                .then()
                .statusCode(404);

        logger.info("Teste concluído: criarPedidoComProdutoIdInexistente");
    }

    @Test(description = "Deve retornar 401 ao criar pedido sem token")
    public void criarPedidoSemToken() {
        logger.info("Executando teste: criarPedidoSemToken");

        Integer produtoId = produtoClient.criarProdutoValido(token);

        pedidoClient.criarPedidoSemToken(pedidoValido(produtoId))
                .then()
                .statusCode(401);

        logger.info("Teste concluído: criarPedidoSemToken");
    }

    // =========================================================
    // GET /pedidos
    // =========================================================

    @Test(description = "Deve listar os pedidos com sucesso - 200")
    public void listarPedidosComSucesso() {
        logger.info("Executando teste: listarPedidosComSucesso");

        pedidoClient.listarPedidos(token)
                .then()
                .statusCode(200);

        logger.info("Teste concluído: listarPedidosComSucesso");
    }

    @Test(description = "Deve listar pedidos com paginacao - 200")
    public void listarPedidosComPaginacao() {
        logger.info("Executando teste: listarPedidosComPaginacao");

        pedidoClient.listarPedidosComPaginacao(token, 0, 5)
                .then()
                .statusCode(200);

        logger.info("Teste concluído: listarPedidosComPaginacao");
    }

    @Test(description = "Deve retornar 200 com lista vazia quando nao ha pedidos cadastrados")
    public void listarPedidosSemRegistros() {
        logger.info("Executando teste: listarPedidosSemRegistros");

        pedidoClient.listarPedidos(token)
                .then()
                .statusCode(200);

        logger.info("Teste concluído: listarPedidosSemRegistros");
    }

    @Test(description = "Deve retornar 401 ao listar pedidos sem token")
    public void listarPedidosSemToken() {
        logger.info("Executando teste: listarPedidosSemToken");

        pedidoClient.listarPedidosSemToken()
                .then()
                .statusCode(401);

        logger.info("Teste concluído: listarPedidosSemToken");
    }

    // =========================================================
    // GET /pedidos/{id}
    // =========================================================

    @Test(description = "Deve buscar um pedido por ID com sucesso - 200")
    public void buscarPedidoPorIdComSucesso() {
        logger.info("Executando teste: buscarPedidoPorIdComSucesso");

        Integer produtoId = produtoClient.criarProdutoValido(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.buscarPedidoPorId(token, pedidoId)
                .then()
                .statusCode(200);

        logger.info("Teste concluído: buscarPedidoPorIdComSucesso");
    }

    @Test(description = "Deve retornar 404 ao buscar pedido com ID inexistente")
    public void buscarPedidoPorIdInexistente() {
        logger.info("Executando teste: buscarPedidoPorIdInexistente");

        pedidoClient.buscarPedidoPorId(token, ID_INEXISTENTE)
                .then()
                .statusCode(404);

        logger.info("Teste concluído: buscarPedidoPorIdInexistente");
    }

    @Test(description = "Deve retornar 400 ao buscar pedido com ID invalido (nao numerico)")
    public void buscarPedidoPorIdInvalido() {
        logger.info("Executando teste: buscarPedidoPorIdInvalido");

        pedidoClient.buscarPedidoPorIdInvalido(token, "abc")
                .then()
                .statusCode(400);

        logger.info("Teste concluído: buscarPedidoPorIdInvalido");
    }

    @Test(description = "Deve retornar 401 ao buscar pedido sem token")
    public void buscarPedidoPorIdSemToken() {
        logger.info("Executando teste: buscarPedidoPorIdSemToken");

        pedidoClient.buscarPedidoPorIdSemToken(ID_INEXISTENTE)
                .then()
                .statusCode(401);

        logger.info("Teste concluído: buscarPedidoPorIdSemToken");
    }

    // =========================================================
    // PUT /pedidos/{id}
    // =========================================================

    @Test(description = "Deve atualizar um pedido com sucesso - 200")
    public void atualizarPedidoComSucesso() {
        logger.info("Executando teste: atualizarPedidoComSucesso");

        Integer produtoId = produtoClient.criarProdutoValido(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        Integer novoProdutoId = produtoClient.criarProdutoValido(token);

        pedidoClient.atualizarPedido(token, pedidoId, pedidoValido(novoProdutoId))
                .then()
                .statusCode(200);

        logger.info("Teste concluído: atualizarPedidoComSucesso");
    }

    @Test(description = "Deve retornar 404 ao atualizar pedido com ID inexistente")
    public void atualizarPedidoComIdInexistente() {
        logger.info("Executando teste: atualizarPedidoComIdInexistente");

        Integer produtoId = produtoClient.criarProdutoValido(token);

        pedidoClient.atualizarPedido(token, ID_INEXISTENTE, pedidoValido(produtoId))
                .then()
                .statusCode(404);

        logger.info("Teste concluído: atualizarPedidoComIdInexistente");
    }

    @Test(description = "Deve retornar 400 ao atualizar pedido com quantidade zero")
    public void atualizarPedidoComQuantidadeZero() {
        logger.info("Executando teste: atualizarPedidoComQuantidadeZero");

        Integer produtoId = produtoClient.criarProdutoValido(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.atualizarPedido(token, pedidoId, pedidoComQuantidadeZero(produtoId))
                .then()
                .statusCode(400);

        logger.info("Teste concluído: atualizarPedidoComQuantidadeZero");
    }

    @Test(description = "Deve retornar 401 ao atualizar pedido sem token")
    public void atualizarPedidoSemToken() {
        logger.info("Executando teste: atualizarPedidoSemToken");

        Integer produtoId = produtoClient.criarProdutoValido(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.atualizarPedidoSemToken(pedidoId, pedidoValido(produtoId))
                .then()
                .statusCode(401);

        logger.info("Teste concluído: atualizarPedidoSemToken");
    }

    // =========================================================
    // DELETE /pedidos/{id}
    // =========================================================

    @Test(description = "Deve cancelar um pedido com status PENDENTE com sucesso - 204")
    public void cancelarPedidoPendenteComSucesso() {
        logger.info("Executando teste: cancelarPedidoPendenteComSucesso");

        Integer produtoId = produtoClient.criarProdutoValido(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.cancelarPedido(token, pedidoId)
                .then()
                .statusCode(204);

        logger.info("Teste concluído: cancelarPedidoPendenteComSucesso");
    }

    @Test(description = "Deve retornar 404 ao cancelar pedido com ID inexistente")
    public void cancelarPedidoComIdInexistente() {
        logger.info("Executando teste: cancelarPedidoComIdInexistente");

        pedidoClient.cancelarPedido(token, ID_INEXISTENTE)
                .then()
                .statusCode(404);

        logger.info("Teste concluído: cancelarPedidoComIdInexistente");
    }

    @Test(description = "Deve retornar 401 ao cancelar pedido sem token")
    public void cancelarPedidoSemToken() {
        logger.info("Executando teste: cancelarPedidoSemToken");

        Integer produtoId = produtoClient.criarProdutoValido(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.cancelarPedidoSemToken(pedidoId)
                .then()
                .statusCode(401);

        logger.info("Teste concluído: cancelarPedidoSemToken");
    }

    @Test(description = "Deve retornar 422 ao cancelar pedido com status que nao permite cancelamento")
    public void cancelarPedidoComStatusQueNaoPermiteCancelamento() {
        logger.info("Executando teste: cancelarPedidoComStatusQueNaoPermiteCancelamento");

        Integer produtoId = produtoClient.criarProdutoValido(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.cancelarPedido(token, pedidoId).then().statusCode(204);

        pedidoClient.cancelarPedido(token, pedidoId)
                .then()
                .statusCode(422);

        logger.info("Teste concluído: cancelarPedidoComStatusQueNaoPermiteCancelamento");
    }
}
