package clients.usuario;

import constants.endpoints.Endpoint;
import factories.usuario.UsuarioFactory;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.request.usuario.UsuarioRequest;
import org.hamcrest.Condition;

import static config.Configuration.getEndpoint;
import static constants.endpoints.Endpoint.*;
import static factories.usuario.UsuarioFactory.*;
import static io.restassured.RestAssured.given;

public class UsuarioClient {

    public Response criarUsuario(UsuarioRequest usuario) {
        return given()
                .contentType(ContentType.JSON)
                .body(usuario)
                .when()
                .post(USUARIOS.getUrl());
    }

    public Response listarUsuarios(String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(USUARIOS.getUrl());
    }

    public Response buscarUsuarioPorNome(String token, String nome) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .queryParam("nome", nome)
                .when()
                .get(USUARIOS.getUrl());
    }

    public Response buscarUsuarioPorId(String token, Integer usuarioId) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(USUARIOS.getUrl() + "/" + usuarioId);
    }

    public Integer criarUsuarioERetornarId(UsuarioRequest usuario) {
        return criarUsuario(usuario)
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    public Response criarUsuarioERetornarPorNome(UsuarioRequest usuario){
        return criarUsuario(usuario)
                .then()
                .statusCode(201)
                .extract()
                .response();
    }


}
