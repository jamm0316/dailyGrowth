package com.todoservice.greencatsoftware.config.security.token;

import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.exception.BaseException;
import com.todoservice.greencatsoftware.common.util.FingerprintUtil;
import com.todoservice.greencatsoftware.config.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class TokenService {
    private final JwtProvider jwtProvider;
    private final String RT_PREFIX = "rt";
    private final String H_REFRESH_TOKEN = "refreshToken";
    private final String H_UA_HASH = "uaHash";
    private final String H_IP_PREFIX = "ipPrefix";

    public TokenResponse generateTokenPair(String userId, String uaHash, String ipPrefix) {
        String accessToken = jwtProvider.generateToken(userId);
        String deviceId = FingerprintUtil.deviceId(uaHash, ipPrefix);

        String key = rtKey(userId, deviceId);
        String refreshToken = UUID.randomUUID().toString();

        return new TokenResponse(accessToken, refreshToken);
    }

    public String reissueAccessToken(String accessToken, String refreshToken, String uaHashNow, String ipPrefixNow) {
        String userIdFromToken = jwtProvider.getUserIdFromToken(accessToken);
        String deviceId = FingerprintUtil.deviceId(uaHashNow, ipPrefixNow);
        String key = rtKey(userIdFromToken, deviceId);

        return jwtProvider.generateToken(userIdFromToken);
    }

    public String rtKey(String userId, String deviceId) {
        return RT_PREFIX + ":" + userId + ":" + deviceId;
    }

    private void validateFingerprintAndRt(
            String userId,
            String deviceId,
            String refreshTokenFromCookie,
            String uaHashNow,
            String ipPrefixNow,
            Map<Object, Object> stored) {
        String rtStored = toStr(stored.get(H_REFRESH_TOKEN));
        String uaStored = toStr(stored.get(H_UA_HASH));
        String ipStored = toStr(stored.get(H_IP_PREFIX));

        // 존재 여부 먼저 체크(NPE 방지 및 명확한 에러)
        if (rtStored == null || uaStored == null || ipStored == null) {
            log.warn("RT record missing fields: userId = {}, deviceId={}, fields={}", userId, deviceId, stored.keySet());
            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }

        boolean mismatch =
                !refreshTokenFromCookie.equals(rtStored) ||
                        !uaHashNow.equals(uaStored) ||
                        !ipPrefixNow.equals(ipStored);

        if (mismatch) {
            log.warn("RefreshToken missmatch: userId={}, deviceId={}", userId, deviceId);
            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }
    }

    private static String toStr(Object o) {
        return (o == null) ? null : o.toString();
    }
}
