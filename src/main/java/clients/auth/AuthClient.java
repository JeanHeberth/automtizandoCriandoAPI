package clients.auth;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.request.usuario.UsuarioRequest;

import static constants.endpoints.Endpoint.LOGIN;
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

    public Response criarUsuario(UsuarioRequest usuario) {
        return given()
                .contentType(ContentType.JSON)
                .body(usuario)
                .when()
                .post(USUARIOS.getUrl());
    }
}