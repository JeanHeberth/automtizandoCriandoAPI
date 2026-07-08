package utils;

import java.util.UUID;

public final class RandomUtils {

    private RandomUtils() {}

    public static String unique() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}