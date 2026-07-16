package pedido;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import base.BaseTest;
import clients.auth.AuthClient;
import clients.produto.PedidoClient;
import clients.produto.ProdutoClient;
import static constants.Ids.ID_INEXISTENTE;
import static factories.pedido.PedidoFactory.pedidoComListaDeItensVazia;
import static factories.pedido.PedidoFactory.pedidoComProdutoIdInexistente;
import static factories.pedido.PedidoFactory.pedidoComQuantidadeNegativa;
import static factories.pedido.PedidoFactory.pedidoComQuantidadeMaiorQueEstoque;
import static factories.pedido.PedidoFactory.pedidoComQuantidadeZero;
import static factories.pedido.PedidoFactory.pedidoComMultiplosItens;
import static factories.pedido.PedidoFactory.pedidoValido;
import static factories.produto.ProdutoFactory.produtoValido;
import static factories.usuario.UsuarioFactory.usuarioValido;
import models.request.pedido.PedidoRequest;
import models.request.produto.ProdutoRequest;
import models.request.usuario.UsuarioRequest;

@Test(groups = "pedidos")
public class PedidoTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(PedidoTest.class);
    private final AuthClient authClient = new AuthClient();
    private final PedidoClient pedidoClient = new PedidoClient();
    private final ProdutoClient produtoClient = new ProdutoClient();

    // =========================================================
    // POST /pedidos
    // =========================================================

    @Test(description = "Deve criar um pedido com sucesso - 201")
    public void criarPedidoComSucesso() {
        logger.info("Executando teste: criarPedidoComSucesso");

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);
        PedidoRequest pedidoRequest = pedidoValido(produtoId);
        Integer pedidoId = pedidoClient.criarPedidoERetornarId(token, pedidoRequest);

        pedidoClient.buscarPedidoPorId(token, pedidoId)
                .then()
                .statusCode(200);

        logger.info("Teste concluído: criarPedidoComSucesso");
    }

    @Test(description = "Deve criar um pedido com multiplos itens com sucesso - 201")
    public void criarPedidoComMultiplosItensComSucesso() {
        logger.info("Executando teste: criarPedidoComMultiplosItensComSucesso");

        Integer produtoIdPrimeiro = criarProdutoComEstoqueSuficiente(token);
        Integer produtoIdSegundo = criarProdutoComEstoqueSuficiente(token);

        PedidoRequest pedidoRequest = pedidoComMultiplosItens(produtoIdPrimeiro, produtoIdSegundo);
        Integer pedidoId = pedidoClient.criarPedidoERetornarId(token, pedidoRequest);

        pedidoClient.buscarPedidoPorId(token, pedidoId)
                .then()
                .statusCode(200);

        logger.info("Teste concluído: criarPedidoComMultiplosItensComSucesso");
    }

    @Test(description = "Deve retornar 422 ao criar pedido com quantidade acima do estoque")
    public void criarPedidoComQuantidadeAcimaDoEstoque() {
        logger.info("Executando teste: criarPedidoComQuantidadeAcimaDoEstoque");

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);
        Integer estoqueAtual = produtoClient.buscarProdutoPorId(produtoId)
                .then()
                .statusCode(200)
                .extract()
                .path("estoque");
        Integer quantidadeAcimaDoEstoque = estoqueAtual + 1;

        pedidoClient.criarPedido(token, pedidoComQuantidadeMaiorQueEstoque(produtoId, quantidadeAcimaDoEstoque))
                .then()
                .statusCode(422);

        logger.info("Teste concluído: criarPedidoComQuantidadeAcimaDoEstoque");
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

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);

        pedidoClient.criarPedido(token, pedidoComQuantidadeZero(produtoId))
                .then()
                .statusCode(400);

        logger.info("Teste concluído: criarPedidoComQuantidadeZero");
    }

    @Test(description = "Deve retornar 400 ao criar pedido com quantidade negativa")
    public void criarPedidoComQuantidadeNegativa() {
        logger.info("Executando teste: criarPedidoComQuantidadeNegativa");

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);

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

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);

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

    @Test(description = "Deve listar pedidos filtrando por status")
    public void listarPedidosComStatus() {
        logger.info("Executando teste: listarPedidosComStatus");

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);
        pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.listarPedidosComStatus(token, "PENDENTE")
                .then()
                .statusCode(200);

        logger.info("Teste concluído: listarPedidosComStatus");
    }

    @Test(description = "Deve retornar 400 ao listar pedidos com status inválido")
    public void listarPedidosComStatusInvalido() {
        logger.info("Executando teste: listarPedidosComStatusInvalido");

        pedidoClient.listarPedidosComStatus(token, "INVALIDO")
                .then()
                .statusCode(400);

        logger.info("Teste concluído: listarPedidosComStatusInvalido");
    }

    @Test(description = "Deve retornar 401 ao listar pedidos por status sem token")
    public void listarPedidosComStatusSemToken() {
        logger.info("Executando teste: listarPedidosComStatusSemToken");

        pedidoClient.listarPedidosComStatusSemToken("PENDENTE")
                .then()
                .statusCode(401);

        logger.info("Teste concluído: listarPedidosComStatusSemToken");
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

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);
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

    @Test(description = "Deve retornar 404 ao buscar pedido de outro usuário")
    public void buscarPedidoPorOutroUsuarioDeveRetornar404() {
        logger.info("Executando teste: buscarPedidoPorOutroUsuarioDeveRetornar404");

        UsuarioRequest outroUsuario = usuarioValido();
        authClient.registro(outroUsuario)
                .then()
                .statusCode(201);

        String outroToken = authClient.login(outroUsuario.getEmail(), outroUsuario.getSenha())
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        Integer produtoId = criarProdutoComEstoqueSuficiente(outroToken);
        Integer pedidoId = pedidoClient.criarPedidoValido(outroToken, produtoId);

        pedidoClient.buscarPedidoPorId(token, pedidoId)
                .then()
                .statusCode(404);

        logger.info("Teste concluído: buscarPedidoPorOutroUsuarioDeveRetornar404");
    }

    // =========================================================
    // DELETE /pedidos/{id}
    // =========================================================

    @Test(description = "Deve cancelar um pedido com status PENDENTE com sucesso - 204")
    public void cancelarPedidoPendenteComSucesso() {
        logger.info("Executando teste: cancelarPedidoPendenteComSucesso");

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.cancelarPedido(token, pedidoId)
                .then()
                .statusCode(204);

        logger.info("Teste concluído: cancelarPedidoPendenteComSucesso");
    }

    @Test(description = "Deve retornar 422 ao cancelar um pedido com status CONFIRMADO")
    public void cancelarPedidoConfirmadoComSucesso() {
        logger.info("Executando teste: cancelarPedidoConfirmadoComSucesso");

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.transicionarStatus(token, pedidoId, "CONFIRMADO")
                .then()
                .statusCode(200);

        pedidoClient.cancelarPedido(token, pedidoId)
                .then()
                .statusCode(422);

        logger.info("Teste concluído: cancelarPedidoConfirmadoComSucesso");
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

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.cancelarPedidoSemToken(pedidoId)
                .then()
                .statusCode(401);

        logger.info("Teste concluído: cancelarPedidoSemToken");
    }

    @Test(description = "Deve retornar 422 ao cancelar pedido com status que nao permite cancelamento")
    public void cancelarPedidoComStatusQueNaoPermiteCancelamento() {
        logger.info("Executando teste: cancelarPedidoComStatusQueNaoPermiteCancelamento");

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.cancelarPedido(token, pedidoId).then().statusCode(204);

        pedidoClient.cancelarPedido(token, pedidoId)
                .then()
                .statusCode(422);

        logger.info("Teste concluído: cancelarPedidoComStatusQueNaoPermiteCancelamento");
    }

    @Test(description = "Deve transicionar um pedido ate ENTREGUE com sucesso")
    public void transicionarPedidoAteEntregueComSucesso() {
        logger.info("Executando teste: transicionarPedidoAteEntregueComSucesso");

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.transicionarStatus(token, pedidoId, "CONFIRMADO")
                .then()
                .statusCode(200);
        pedidoClient.transicionarStatus(token, pedidoId, "EM_PREPARO")
                .then()
                .statusCode(200);
        pedidoClient.transicionarStatus(token, pedidoId, "ENVIADO")
                .then()
                .statusCode(200);
        pedidoClient.transicionarStatus(token, pedidoId, "ENTREGUE")
                .then()
                .statusCode(200);

        logger.info("Teste concluído: transicionarPedidoAteEntregueComSucesso");
    }

    @Test(description = "Deve retornar 422 ao transicionar um pedido para um status invalido")
    public void transicionarPedidoComStatusInvalido() {
        logger.info("Executando teste: transicionarPedidoComStatusInvalido");

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.transicionarStatus(token, pedidoId, "ENVIADO")
                .then()
                .statusCode(422);

        logger.info("Teste concluído: transicionarPedidoComStatusInvalido");
    }

    @Test(description = "Deve retornar 401 ao transicionar pedido sem token")
    public void transicionarPedidoSemToken() {
        logger.info("Executando teste: transicionarPedidoSemToken");

        Integer produtoId = criarProdutoComEstoqueSuficiente(token);
        Integer pedidoId = pedidoClient.criarPedidoValido(token, produtoId);

        pedidoClient.transicionarStatusSemToken(pedidoId, "CONFIRMADO")
                .then()
                .statusCode(401);

        logger.info("Teste concluído: transicionarPedidoSemToken");
    }

    private Integer criarProdutoComEstoqueSuficiente(String tokenAutenticacao) {
        ProdutoRequest produto = produtoValido();
        produto.setEstoque(10);
        return produtoClient.criarProdutoERetornarId(tokenAutenticacao, produto);
    }
}
