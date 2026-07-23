package config;

public final class Environment {

    private Environment() {
    }

    public static String getEmail() {
        String email = System.getenv("API_EMAIL");

        if (email == null || email.isBlank()) {
            throw new IllegalStateException(
                    "Variável de ambiente API_EMAIL não encontrada."
            );
        }

        email = email.trim();

        System.out.println("API_EMAIL recebida: SIM");
        System.out.println("Tamanho do email: " + email.length());
        System.out.println("Email possui @: " + email.contains("@"));

        return email;
    }

    public static String getSenha() {
        String senha = System.getenv("API_SENHA");

        if (senha == null || senha.isBlank()) {
            throw new IllegalStateException(
                    "Variável de ambiente API_SENHA não encontrada."
            );
        }

        System.out.println("API_SENHA recebida: SIM");
        System.out.println("Tamanho da senha: " + senha.length());

        return senha;
    }
}