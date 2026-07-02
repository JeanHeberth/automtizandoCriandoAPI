package usuario;

import base.BaseTest;
import clients.usuario.UsuarioClient;
import models.request.usuario.UsuarioRequest;
import org.testng.annotations.Test;

import java.util.logging.Logger;

import static factories.usuario.UsuarioFactory.*;
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

}
