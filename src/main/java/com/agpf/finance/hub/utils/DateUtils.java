package com.agpf.finance.hub.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils {

    public static String ZONE_ID = "America/Sao_Paulo";

    private DateUtils () {}

    public static LocalDate getLocalDateAmericaSP() {
        return LocalDate.now(ZoneId.of(ZONE_ID));
    }

    public static LocalDateTime getLocalDateTimeAmericaSP() {
        return LocalDateTime.now(ZoneId.of(ZONE_ID));
    }

}
