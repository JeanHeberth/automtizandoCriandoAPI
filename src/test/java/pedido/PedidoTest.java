package pedido;

import base.BaseTest;
import clients.produto.PedidoClient;
import clients.produto.ProdutoClient;
import factories.pedido.PedidoFactory;
import factories.produto.ProdutoFactory;
import models.request.pedido.PedidoRequest;
import models.request.produto.ProdutoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static factories.pedido.PedidoFactory.*;

public class PedidoTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(PedidoTest.class);
    private final PedidoClient pedidoClient = new PedidoClient();


    @Test(description = "Deve criar um pedido com sucesso e validar o status do pedido")
    public void criarPedidoComSucesso() {

        logger.info("Executando teste: criarPedidoComSucesso");

        Integer produtoId = new ProdutoClient().criarProdutoERetornarId(token,
                ProdutoFactory.produtoValido()
        );

        PedidoRequest pedidoRequest = pedidoValido(produtoId);

        Integer pedidoId = pedidoClient.criarPedidoERetornarId(token,
                pedidoRequest
        );

        pedidoClient.buscarPedidoPorId(token, pedidoId)
                .then()
                .statusCode(200);

        logger.info("Teste concluído: criarPedidoComSucesso");

    }

    @Test(description = "Deve listar os pedidos com sucesso")
    public void listarPedidosComSucesso() {

        logger.info("Executando teste: listarPedidosComSucesso");

        pedidoClient.listarPedidos(token)
                .then()
                .statusCode(200);

        logger.info("Teste concluído: listarPedidosComSucesso");
    }

    @Test(description = "Deve buscar um pedido por ID com sucesso")
    public void buscarPedidoPorIdComSucesso() {

        logger.info("Executando teste: buscarPedidoPorIdComSucesso");

        Integer produtoId = new ProdutoClient().criarProdutoERetornarId(token,
                ProdutoFactory.produtoValido()
        );

        PedidoRequest pedidoRequest = pedidoValido(produtoId);

        Integer pedidoId = pedidoClient.criarPedidoERetornarId(token,
                pedidoRequest
        );

        pedidoClient.buscarPedidoPorId(token, pedidoId)
                .then()
                .statusCode(200);

        logger.info("Teste concluído: buscarPedidoPorIdComSucesso");
    }

    @Test(description = "Deve validar a criação de um pedido com a quantidade vazia")
    public void criarPedidoComQuantidadeVazia() {

        logger.info("Executando teste: criarPedidoComQuantidadeVazia");

        Integer produtoId = new ProdutoClient().criarProdutoERetornarId(token,
                ProdutoFactory.produtoValido()
        );
        PedidoRequest pedidoRequest = pedidoComQuantidadeVazia(produtoId);
        pedidoClient.criarPedido(token, pedidoRequest)
                .then()
                .statusCode(400);

        logger.info("Teste concluído: criarPedidoComQuantidadeVazia");
    }


}