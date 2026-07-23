package config;

public final class Environment {

    private Environment() {
    }

    public static String getEmail() {
        String email = System.getenv("API_USER");

        if (email == null || email.isBlank()) {
            throw new IllegalStateException(
                    "A variável API_USER não foi encontrada."
            );
        }

        System.out.println("API_USER recebida: SIM");
        System.out.println("Tamanho do email: " + email.length());
        System.out.println("Email termina com @gmail.com: "
                + email.endsWith("@gmail.com"));

        return email;
    }

    public static String getSenha() {
        String senha = System.getenv("API_USER_PSW");

        if (senha == null || senha.isBlank()) {
            throw new IllegalStateException(
                    "A variável API_USER_PSW não foi encontrada."
            );
        }

        System.out.println("API_USER_PSW recebida: SIM");
        System.out.println("Tamanho da senha: " + senha.length());

        return senha;
    }
}