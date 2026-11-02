package org.example.utils;

import java.util.Locale;

public class CsvUtils {

    public static String escape(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"")
                || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public static String number(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    public static String header(String... cols) {
        return String.join(",", cols) + "\n";
    }

    public static String line(Object... values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) sb.append(",");
            Object v = values[i];
            if (v == null) sb.append("");
            else if (v instanceof Double || v instanceof Float) sb.append(number((Double) v));
            else sb.append(escape(v.toString()));
        }
        return sb.append("\n").toString();
    }
}