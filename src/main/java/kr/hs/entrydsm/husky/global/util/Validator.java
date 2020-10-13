package kr.hs.entrydsm.husky.global.util;

import java.math.BigDecimal;

public class Validator {
    public static boolean isExists(String target) {
        return (target != null) && (!target.isBlank());
    }

    public static boolean isGreaterThanOrEqualTo(Integer target, Integer standard) {
        return (target != null) && (target >= standard);
    }

    public static boolean isGreaterThanOrEqualTo(BigDecimal target, BigDecimal standard) {
        return (target != null) && (target.compareTo(standard) >= 0);
    }

    public static boolean isEqualTo(Integer target, Integer standard) {
        return (target != null) && (target.equals(standard));
    }

    public static boolean isPositive(BigDecimal target) {
        return (target != null) && (target.compareTo(BigDecimal.ZERO) > 0);
    }

    public static boolean isZero(BigDecimal target) {
        return (target != null) && (target.compareTo(BigDecimal.ZERO) == 0);
    }

}
