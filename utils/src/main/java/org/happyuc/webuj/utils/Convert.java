package org.happyuc.webuj.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * HappyUC unit conversion functions.
 */
public final class Convert {
    private Convert() {
    }

    public static BigDecimal fromWei(String number, Unit unit) {
        return fromWei(new BigDecimal(number), unit);
    }

    public static BigDecimal fromWei(BigDecimal number, Unit unit) {
        return number.divide(unit.getWeiFactor(), RoundingMode.HALF_UP);
    }

    public static BigInteger fromWei(BigInteger number, Unit unit) {
        return fromWei(new BigDecimal(number), unit).toBigInteger();
    }

    public static BigDecimal toWei(String number, Unit unit) {
        return toWei(new BigDecimal(number), unit);
    }

    public static BigDecimal toWei(BigDecimal number, Unit unit) {
        return number.multiply(unit.getWeiFactor());
    }

    public static BigInteger toWei(BigInteger number, Unit unit) {
        return toWei(new BigDecimal(number), unit).toBigInteger();
    }


    public enum Unit {
        WEI("wei", 0),
        KWEI("kwei", 3),
        MWEI("mwei", 6),
        GWEI("gwei", 9),
        TWEI("twei", 12),
        PWEI("pwei", 15),
        HUC("huc", 18),
        KHUC("khuc", 21),
        MHUC("mhuc", 24),
        GHUC("ghuc", 27),
        THUC("thuc", 30),
        PHUC("phuc", 33),
        EHUC("ehuc", 36),
        ZHUC("zhuc", 39),
        YHUC("yhuc", 42),
        NHUC("nhuc", 45),
        DHUC("dhuc", 48),
        VHUC("vhuc", 51),
        UHUC("uhuc", 54);

        private String name;
        private BigDecimal weiFactor;

        Unit(String name, int factor) {
            this.name = name;
            this.weiFactor = BigDecimal.TEN.pow(factor);
        }

        public BigDecimal getWeiFactor() {
            return weiFactor;
        }

        @Override
        public String toString() {
            return name;
        }

        public static Unit fromString(String name) {
            if (name != null) {
                for (Unit unit : Unit.values()) {
                    if (name.equalsIgnoreCase(unit.name)) {
                        return unit;
                    }
                }
            }
            return Unit.valueOf(name);
        }
    }
}
