package cn.zzz.demo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class OrderNumberGenerator {
    public static String generateOrderNumber(LocalDateTime time) {
        return "NO" + time.format(DateTimeFormatter.ofPattern("yyMM")) + UUID.randomUUID();
    }

    public static String generateOrderNumber(LocalDate date) {
        return "NO" + date.format(DateTimeFormatter.ofPattern("yyMM")) + UUID.randomUUID();
    }

    public static String getYYMMFromOrderNumber(String orderNumber) {
        return orderNumber.substring(2, 6);
    }
}
