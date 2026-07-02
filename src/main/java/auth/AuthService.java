package auth;

import clients.auth.AuthClient;
import config.Environment;
import io.restassured.response.Response;
import models.request.usuario.UsuarioRequest;

import static config.Environment.getEmail;
import static config.Environment.getSenha;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

public class AuthService {

    private static final int SUCCESS_STATUS = 200;
    private static final String TOKEN_PATH = "token";
    private static String cachedToken;
    private static final AuthClient authClient = new AuthClient();

    private AuthService() {
    }

    public static String getToken() {
        if (cachedToken != null && !cachedToken.isBlank()) {
            return cachedToken;
        }

        cachedToken = fetchTokenComBootstrap(getEmail(), getSenha());
        return cachedToken;
    }

    public static String getToken(String email, String password) {
        validateCredentials(email, password);
        return fetchTokenComBootstrap(email, password);
    }

    public static void clearCache() {
        cachedToken = null;
    }

    private static String fetchTokenComBootstrap(String email, String password) {
        Response response = loginRequest(email, password);

        if (response.statusCode() == 401 || response.statusCode() == 404) {
            criarUsuarioPadrao();
            response = loginRequest(email, password);
        }

        return response
                .then()
                .statusCode(SUCCESS_STATUS)
                .extract()
                .path(TOKEN_PATH);
    }

    private static Response loginRequest(String email, String password) {
        return authClient.login(buildLoginBody(email, password));
    }

    private static String buildLoginBody(String email, String password) {
        return """
                {
                  "email": "%s",
                  "senha": "%s"
                }
                """.formatted(email, password);
    }

    private static void validateCredentials(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }
    }

    private static void criarUsuarioPadrao() {
        UsuarioRequest usuario = new UsuarioRequest();
        usuario.setNome("Usuário Automação");
        usuario.setEmail(Environment.getEmail());
        usuario.setSenha(Environment.getSenha());

        authClient.criarUsuario(usuario)
                .then()
                .statusCode(anyOf(equalTo(201), equalTo(409)));
    }
}