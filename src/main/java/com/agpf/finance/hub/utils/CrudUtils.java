package com.agpf.finance.hub.utils;

import java.util.function.Consumer;

public class CrudUtils {

    private CrudUtils() {
    }

    public static <T> void updateField(T fieldValue, Consumer<T> setter) {
        if (fieldValue != null)
            setter.accept(fieldValue);
    }

}
