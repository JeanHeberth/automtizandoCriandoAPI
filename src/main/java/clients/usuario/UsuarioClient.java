package clients.usuario;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.request.usuario.UsuarioRequest;

import static config.Configuration.getEndpoint;
import static io.restassured.RestAssured.given;

public class UsuarioClient {

    public Response criarUsuario(UsuarioRequest usuario) {
        return given()
                .contentType(ContentType.JSON)
                .body(usuario)
                .when()
                .post(getEndpoint("usuarios"));
    }
}
