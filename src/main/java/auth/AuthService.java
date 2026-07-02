package auth;

import config.Environment;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.request.usuario.UsuarioRequest;

import static config.Configuration.getEndpoint;
import static config.Environment.getEmail;
import static config.Environment.getSenha;
import static io.restassured.RestAssured.given;

public class AuthService {

    private static final int SUCCESS_STATUS = 200;
    private static final String TOKEN_PATH = "token";
    private static String cachedToken;

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
        validateCredentials(email, password);

        return given()
                .contentType(ContentType.JSON)
                .body(buildLoginBody(email, password))
                .when()
                .post(getEndpoint("login"));
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

        given()
                .contentType(ContentType.JSON)
                .body(usuario)
                .when()
                .post(getEndpoint("usuarios"))
                .then()
                .statusCode(201);
    }
}