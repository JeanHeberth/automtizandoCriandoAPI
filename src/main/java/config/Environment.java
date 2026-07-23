package config;

public final class Environment {

    private Environment() {
    }

    public static String getEmail() {
        String email = System.getenv("API_EMAIL");

        if (email == null || email.isBlank()) {
            email = Configuration.getUsuario();
        }

        return email;
    }

    public static String getSenha() {
        String senha = System.getenv("API_SENHA");

        if (senha == null || senha.isBlank()) {
            senha = Configuration.getSenha();
        }

        return senha.trim();
    }
}