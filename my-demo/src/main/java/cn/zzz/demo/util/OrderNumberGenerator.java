package cn.zzz.demo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class OrderNumberGenerator {
    public static String generateOrderNumber(LocalDateTime time) {
        return "NO" + time.format(DateTimeFormatter.ofPattern("yyyyMM")) + UUID.randomUUID();
    }

    public static String generateOrderNumber(LocalDate date) {
        return "NO" + date.format(DateTimeFormatter.ofPattern("yyyyMM")) + UUID.randomUUID();
    }

    public static String getYYYYMMFromOrderNumber(String orderNumber) {
        return orderNumber.substring(2, 8);
    }
}
