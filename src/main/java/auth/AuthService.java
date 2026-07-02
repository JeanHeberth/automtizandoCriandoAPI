package auth;

import static config.Configuration.getEndpoint;
import static config.Environment.*;
import static io.restassured.RestAssured.given;

public class AuthService {
    public static String getToken() {
        String body = """
                {
                    "email": "%s",
                    "senha": "%s"
                }
                """.formatted(
                getEmail(),
                getSenha()
        );

        return given()
                .contentType("application/json")
                .body(body)
                .when()
                .post(getEndpoint("login"))
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
}
