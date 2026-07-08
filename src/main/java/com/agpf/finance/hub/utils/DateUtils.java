package com.agpf.finance.hub.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils {

    private DateUtils () {}

    public static LocalDate getLocalDateAmericaSP() {
        return LocalDate.now(ZoneId.of("America/Sao_Paulo"));
    }

    public static LocalDateTime getLocalDateTimeAmericaSP() {
        return LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

}
