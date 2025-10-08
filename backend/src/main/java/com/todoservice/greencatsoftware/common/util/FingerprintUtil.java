package com.todoservice.greencatsoftware.common.util;

import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class FingerprintUtil {
    public static String extractClientIp(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "Forwarded",
                "X-Real-IP",
                "CF-Connecting-IP",
                "X-Client-IP"
        };
        for (String h : headers) {
            String v = request.getHeader(h);
            if (v != null && !v.isBlank()) {
                String first = v.split(",")[0].trim();
                // Forwarded 헤더 형태 보정
                if (first.startsWith("for=")) {
                    first = first.substring(4).trim();
                    if (first.startsWith("\"") && first.endsWith("\"")) {
                        first = first.substring(1, first.length() - 1);
                    }
                    if (first.startsWith("[") && first.contains("]")) {
                        first = first.substring(1, first.indexOf(']')); //[IPv6]
                    }
                }
                return first;
            }
        }
        return request.getRemoteAddr();
    }

    public static String ipPrefix(String ip) {
        if (ip.contains(":")) return ip.split(":")[0] + ":" + ip.split(":")[1];  //IPv6 → 64 정도
        return ip.substring(0, ip.lastIndexOf(":"));
    }

    public static String uaHash(String ua) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(md.digest((ua == null ? "" : ua).toLowerCase().getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            return "ua_unknown";
        }
    }

    public static String deviceId(String uaHash, String ipPrefix) {
        return uaHash + ":" + ipPrefix;
    }
}
