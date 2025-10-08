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
        if (ip == null || ip.isBlank()) return "ip_unknown";
        String s = ip.trim();

        // 1) [IPv6]:port 형태 → 브래킷 제거
        if (s.startsWith("[") && s.contains("]")) {
            s = s.substring(1, s.indexOf(']')); // [::1] → ::1
        }

        // 2) zone-id 제거 (예: fe80::1%eth0)
        int zoneIdx = s.indexOf('%');
        if (zoneIdx != -1) s = s.substring(0, zoneIdx);

        // 3) IPv4 with port → 1.2.3.4:5678 인 경우 포트 제거
        //    (getRemoteAddr()에는 보통 포트가 없지만, 헤더 파서가 포트 포함시킬 수 있어 방어)
        if (s.contains(".") && s.contains(":")) { // 도트와 콜론이 함께 있으면 IPv4:port 로 간주
            s = s.substring(0, s.indexOf(':'));
        }

        // 4) IPv6 처리 (콜론 포함)
        if (s.contains(":")) {
            // hextet 기준으로 /64 정도의 prefix 반환
            String[] parts = s.split(":", -1); // 빈 hextet 허용(::)
            int n = Math.min(4, parts.length); // 첫 4개 hextet
            return String.join(":", java.util.Arrays.copyOfRange(parts, 0, n));
        }

        // 5) IPv4 처리 (도트 포함)
        if (s.contains(".")) {
            String[] parts = s.split("\\.");
            // /24 느낌으로 앞 3옥텟
            if (parts.length >= 3) return parts[0] + "." + parts[1] + "." + parts[2];
            return s; // 형식 이상하면 원본 반환
        }

        // 6) 그 외 (알 수 없는 형식)
        return s;
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
