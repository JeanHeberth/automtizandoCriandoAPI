package auth;

import base.BaseTest;
import clients.auth.AuthClient;
import models.request.usuario.UsuarioRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static factories.usuario.UsuarioFactory.usuarioValido;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

@Test(groups = "autenticacao")
public class AuthTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(AuthTest.class);
    private final AuthClient authClient = new AuthClient();

    @Test(description = "Deve criar uma conta e retornar token com sucesso")
    public void registroComSucesso() {
        logger.info("Executando teste: registroComSucesso");

        UsuarioRequest usuario = usuarioValido();

        authClient.registro(usuario)
                .then()
                .statusCode(201);

        logger.info("Teste concluído: registroComSucesso");
    }

    @Test(description = "Deve retornar 409 ao registrar uma conta com email duplicado")
    public void registroComEmailDuplicado() {
        logger.info("Executando teste: registroComEmailDuplicado");

        UsuarioRequest usuario = usuarioValido();

        authClient.registro(usuario)
                .then()
                .statusCode(201);

        authClient.registro(usuario)
                .then()
                .statusCode(409);

        logger.info("Teste concluído: registroComEmailDuplicado");
    }

    @Test(description = "Deve autenticar com sucesso e retornar token")
    public void loginComSucesso() {
        logger.info("Executando teste: loginComSucesso");

        UsuarioRequest usuario = usuarioValido();
        authClient.registro(usuario)
                .then()
                .statusCode(201);

        authClient.login(usuario.getEmail(), usuario.getSenha())
                .then()
                .statusCode(200)
                .body("token", not(isEmptyOrNullString()));

        logger.info("Teste concluído: loginComSucesso");
    }

    @Test(description = "Deve retornar 401 ao autenticar com credenciais inválidas")
    public void loginComCredenciaisInvalidas() {
        logger.info("Executando teste: loginComCredenciaisInvalidas");

        authClient.login("inexistente@" + System.currentTimeMillis() + ".com", "SenhaSegura123!")
                .then()
                .statusCode(401);

        logger.info("Teste concluído: loginComCredenciaisInvalidas");
    }
}
