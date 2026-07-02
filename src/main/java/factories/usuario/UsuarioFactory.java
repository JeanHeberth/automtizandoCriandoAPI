package factories.usuario;

import models.request.usuario.UsuarioRequest;

public class UsuarioFactory {

    public static UsuarioRequest usuarioValido() {
        UsuarioRequest usuario = new UsuarioRequest();
        usuario.setNome("Usuario Teste");
        usuario.setEmail("usuario" + System.currentTimeMillis() + "@email.com");
        usuario.setSenha("SenhaSegura123!");
        return usuario;
    }

    public static UsuarioRequest usuarioNomeVazio() {
        UsuarioRequest usuario = usuarioValido();
        usuario.setNome("");
        return usuario;
    }

    public static UsuarioRequest usuarioNomeComCaractereMenorQueOPermitido() {
        UsuarioRequest usuario = usuarioValido();
        usuario.setNome("A");
        return usuario;
    }

    public static UsuarioRequest usuarioComEmailInvalido() {
        UsuarioRequest usuario = usuarioValido();
        usuario.setEmail("emailinvalido");
        return usuario;
    }

    public static UsuarioRequest usuarioComEmailVazio() {
        UsuarioRequest usuario = usuarioValido();
        usuario.setEmail("");
        return usuario;
    }

    public static UsuarioRequest usuarioComSenhaVazia() {
        UsuarioRequest usuario = usuarioValido();
        usuario.setSenha("");
        return usuario;
    }

    public static UsuarioRequest usuarioComSenhaComCaractereMenorQueOPermitido() {
        UsuarioRequest usuario = usuarioValido();
        usuario.setSenha("12345");
        return usuario;
    }
}
