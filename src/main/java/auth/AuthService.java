package auth;

import static config.Configuration.getEndpoint;
import static config.Environment.*;
import static io.restassured.RestAssured.given;

import io.restassured.response.Response;

public class AuthService {

    private static final String CONTENT_TYPE = "application/json";
    private static final int SUCCESS_STATUS = 200;
    private static final String TOKEN_PATH = "token";
    private static String cachedToken;

    public static String getToken() {
        if (cachedToken != null && !cachedToken.isEmpty()) {
            return cachedToken;
        }
        cachedToken = fetchToken(getEmail(), getSenha());
        return cachedToken;
    }

    public static String getToken(String email, String password) {
        validateCredentials(email, password);
        return fetchToken(email, password);
    }

    public static boolean login(String email, String password) {
        validateCredentials(email, password);
        try {
            fetchToken(email, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void clearCache() {
        cachedToken = null;
    }

    private static String fetchToken(String email, String password) {
        String body = buildLoginBody(email, password);

        Response response = given()
                .contentType(CONTENT_TYPE)
                .body(body)
                .when()
                .post(getEndpoint("login"))
                .then()
                .statusCode(SUCCESS_STATUS)
                .extract()
                .response();

        return response.path(TOKEN_PATH);
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
}
