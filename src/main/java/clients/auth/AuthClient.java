package clients.auth;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.request.usuario.UsuarioRequest;

import static constants.endpoints.Endpoint.LOGIN;
import static constants.endpoints.Endpoint.REGISTRO;
import static constants.endpoints.Endpoint.USUARIOS;
import static io.restassured.RestAssured.given;

public class AuthClient {

    public Response login(String body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(LOGIN.getUrl());
    }

    public Response login(String email, String senha) {
        return given()
                .contentType(ContentType.JSON)
                .body(buildLoginBody(email, senha))
                .when()
                .post(LOGIN.getUrl());
    }

    public Response registro(UsuarioRequest usuario) {
        return given()
                .contentType(ContentType.JSON)
                .body(usuario)
                .when()
                .post(REGISTRO.getUrl());
    }

    public Response criarUsuario(UsuarioRequest usuario) {
        return given()
                .contentType(ContentType.JSON)
                .body(usuario)
                .when()
                .post(USUARIOS.getUrl());
    }

    private String buildLoginBody(String email, String senha) {
        return """
                {
                  "email": "%s",
                  "senha": "%s"
                }
                """.formatted(email, senha);
    }
}
