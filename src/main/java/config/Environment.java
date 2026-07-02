package config;

import io.github.cdimascio.dotenv.Dotenv;

public class Environment {

    private static final Dotenv dotenv = Dotenv
            .configure()
            .ignoreIfMissing()
            .load();

    public static String getEmail() {
        return dotenv.get("API_EMAIL");
    }

    public static String getSenha() {
        return dotenv.get("API_SENHA");
    }
}