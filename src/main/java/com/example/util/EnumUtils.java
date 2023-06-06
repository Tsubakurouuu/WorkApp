package com.example.util;

public class EnumUtils {
    public static <E extends Enum<E>> E fromValue(Class<E> enumClass, int value) {
        return enumClass.getEnumConstants()[value];
    }
}
