package com.samvpn.app.Utils;

import java.util.Locale;

public class LocalFormatter {
    public static String easyRead(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.ENGLISH, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String byteCounter(long bytes) {
        return String.valueOf(String.format(Locale.getDefault(), "%.0f", (double) bytes / 1024 / 1024));
    }
}
