package by.ivanshka.roomchat.common.util;

public class StringUtils {
    public static boolean notNullOrEmpty(String s) {
        return s != null && !s.isEmpty();
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
