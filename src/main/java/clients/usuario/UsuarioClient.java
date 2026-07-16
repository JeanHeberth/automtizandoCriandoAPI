package clients.usuario;

import static constants.endpoints.Endpoint.USUARIOS;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.request.usuario.UsuarioRequest;

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

    public Response atualizarUsuario(String token, Integer usuarioId, UsuarioRequest usuario) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(usuario)
                .when()
                .put(USUARIOS.getUrl() + "/" + usuarioId);
    }

    public Response atualizarUsuarioSemToken(Integer usuarioId, UsuarioRequest usuario) {
        return given()
                .contentType(ContentType.JSON)
                .body(usuario)
                .when()
                .put(USUARIOS.getUrl() + "/" + usuarioId);
    }

    public Response deletarUsuario(String token, Integer usuarioId) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(USUARIOS.getUrl() + "/" + usuarioId);
    }

    public Response deletarUsuarioSemToken(Integer usuarioId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete(USUARIOS.getUrl() + "/" + usuarioId);
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


    public Response buscarUsuarioPorNomeInexistente(String token, String nomeInexistente) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(USUARIOS.getUrl() + "/" + "buscar?nome=" + nomeInexistente);
    }
}
