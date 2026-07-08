package usuario;

import base.BaseTest;
import clients.usuario.UsuarioClient;
import models.request.usuario.UsuarioRequest;
import org.testng.annotations.Test;

import java.util.logging.Logger;

import static constants.Ids.ID_INEXISTENTE;
import static constants.Ids.NOME_INEXISTENTE;
import static factories.usuario.UsuarioFactory.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;

public class UsuarioTest extends BaseTest {

    private static final Logger logger = Logger.getLogger(UsuarioTest.class.getName());
    private final UsuarioClient usuarioClient = new UsuarioClient();


    @Test(description = "Deve realizar a criação de um usuário com sucesso")
    public void testCriarUsuario() {

        logger.info("Executando teste de criação de usuário");

        UsuarioRequest usuario = usuarioValido();
        usuarioClient.criarUsuario(usuario)
                .then()
                .statusCode(201);

        logger.info("Usuário criado: " + usuario.getNome());
    }

    @Test(description = "Deve falhar ao criar um usuário com nome vazio")
    public void testCriarUsuarioComNomeVazio() {
        logger.info("Executando teste de criação de usuário com nome vazio");

        usuarioClient.criarUsuario(usuarioNomeVazio())
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Nome é obrigatório"));

        logger.info("Falha ao criar usuário com nome vazio: " + usuarioNomeVazio().getNome());
    }

    @Test(description = "Deve falhar ao criar um usuário com nome com menos de 2 caracteres")
    public void testCriarUsuarioComNomeComCaractereMenorQueOPermitido() {
        logger.info("Executando teste de criação de usuário com nome com menos de 2 caracteres");

        usuarioClient.criarUsuario(usuarioNomeComCaractereMenorQueOPermitido())
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Nome deve ter entre 3 e 100 caracteres"));

        logger.info("Falha ao criar usuário com nome com menos de 2 caracteres: " + usuarioNomeComCaractereMenorQueOPermitido().getNome());
    }

    @Test(description = "Deve falhar ao criar um usuário com email inválido")
    public void testCriarUsuarioComEmailInvalido() {
        logger.info("Executando teste de criação de usuário com email inválido");

        usuarioClient.criarUsuario(usuarioComEmailInvalido())
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("E-mail deve ter formato válido"));

        logger.info("Falha ao criar usuário com email inválido: " + usuarioComEmailInvalido().getEmail());
    }

    @Test(description = "Deve falhar ao criar um usuário com email vazio")
    public void testCriarUsuarioComEmailVazio() {
        logger.info("Executando teste de criação de usuário com email vazio");

        usuarioClient.criarUsuario(usuarioComEmailVazio())
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("E-mail é obrigatório"));

        logger.info("Falha ao criar usuário com email vazio: " + usuarioComEmailVazio().getEmail());
    }

    @Test(description = "Deve falhar ao criar um usuário com senha vazia")
    public void testCriarUsuarioComSenhaVazia() {
        logger.info("Executando teste de criação de usuário com senha vazia");

        usuarioClient.criarUsuario(usuarioComSenhaVazia())
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Senha é obrigatória"));

        logger.info("Falha ao criar usuário com senha vazia: " + usuarioComSenhaVazia().getSenha());
    }

    @Test(description = "Deve falhar ao criar um usuário com senha com menos de 6 caracteres")
    public void testCriarUsuarioComSenhaComCaractereMenorQueOPermitido() {
        logger.info("Executando teste de criação de usuário com senha com menos de 6 caracteres");

        usuarioClient.criarUsuario(usuarioComSenhaComCaractereMenorQueOPermitido())
                .then()
                .statusCode(400)
                .body("campos.mensagem", hasItem("Senha deve ter no mínimo 6 caracteres"));

        logger.info("Falha ao criar usuário com senha com menos de 6 caracteres: " + usuarioComSenhaComCaractereMenorQueOPermitido().getSenha());
    }

    @Test(description = "Deve retornar 200 ao listar todos os usuários")
    public void testListarTodosUsuarios() {
        logger.info("Executando teste de listagem de todos os usuários");

        usuarioClient.listarUsuarios(token)
                .then()
                .statusCode(200);

        logger.info("Listagem de usuários realizada com sucesso");
    }

    @Test(description = "Deve falhar ao listar usuários com token inválido")
    public void testListarUsuariosComTokenInvalido() {
        logger.info("Executando teste de listagem de usuários com token inválido");

        usuarioClient.listarUsuarios("token_invalido")
                .then()
                .statusCode(401)
                .body("error", containsString("Unauthorized"));

        logger.info("Falha ao listar usuários com token inválido");
    }

    @Test(description = "Deve falhar ao listar usuários sem token")
    public void testListarUsuariosSemToken() {
        logger.info("Executando teste de listagem de usuários sem token");
        usuarioClient.listarUsuarios(null)
                .then()
                .statusCode(401)
                .body("error", containsString("Unauthorized"));

        logger.info("Falha ao listar usuários sem token");
    }

    @Test(description = "Deve retornar 200 ao buscar usuario por id")
    public void testBuscarUsuarioPorId() {
        logger.info("Executando teste de busca de usuário por ID");

        Integer usuarioId = usuarioClient.criarUsuarioERetornarId(usuarioValido());
        usuarioClient.buscarUsuarioPorId(token, usuarioId)
                .then()
                .statusCode(200);

        logger.info("Busca de usuário " + usuarioId + " realizada com sucesso");
    }

    @Test(description = "Deve falhar ao buscar usuario por id inexistente")
    public void testBuscarUsuarioPorIdInexistente() {
        logger.info("Executando teste de busca de usuário por ID inexistente");
        usuarioClient.buscarUsuarioPorId(token, ID_INEXISTENTE)
                .then()
                .statusCode(404)
                .body("mensagem", containsString("Usuario nao encontrado com id: " + ID_INEXISTENTE));

        logger.info("Falha ao buscar usuário " + ID_INEXISTENTE + " inexistente");
    }

    @Test(description = "Deve retornar 200 ao buscar um usuario por nome")
    public void testBuscarUsuarioPorNome() {
        logger.info("Executando teste de busca de usuário por nome");

        UsuarioRequest usuario = usuarioValido();

        usuarioClient.criarUsuarioERetornarPorNome(usuario)
                .then()
                .statusCode(201);

        usuarioClient.buscarUsuarioPorNome(token, usuario.getNome())
                .then()
                .statusCode(200)
                .body("nome", hasItem(usuario.getNome()));

        logger.info("Busca de usuário por nome " + usuario.getNome() + " realizada com sucesso");
    }

    @Test(description = "Deve falhar ao buscar usuário por nome inexistente")
    public void testBuscarUsuarioPorNomeInexistente() {
        logger.info("Executando teste de busca de usuário por nome inexistente");
        usuarioClient.buscarUsuarioPorNomeInexistente(token, NOME_INEXISTENTE)
                .then()
                .statusCode(404)
                .body("mensagem", containsString("Nenhum usuario encontrado com nome: " + NOME_INEXISTENTE));

        logger.info("Falha ao buscar usuário por nome inexistente");
    }

}
