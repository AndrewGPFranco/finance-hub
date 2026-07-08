package com.agpf.finance.hub.utils;

import java.util.function.Consumer;

public class UtilsCrud {

    private UtilsCrud() {
    }

    public static <T> void updateField(T fieldValue, Consumer<T> setter) {
        if (fieldValue != null)
            setter.accept(fieldValue);
    }

}
