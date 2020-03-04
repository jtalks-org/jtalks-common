package ru.javatalks.utils.general;

public class Assert {
    public static void throwIfNull(Object v, String message) {
        if(v == null)
            throw new IllegalArgumentException(message);
    }
}
