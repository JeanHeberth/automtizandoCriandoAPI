package auth;

public class TokenManager {

    private static String token;

    private TokenManager() {
    }

    public static String getToken() {

        if (token == null || token.isBlank()) {
            token = AuthService.getToken();
        }

        return token;
    }

    public static void resetToken() {
        token = null;
    }
}