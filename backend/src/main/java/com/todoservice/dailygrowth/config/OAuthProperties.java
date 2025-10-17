package com.todoservice.dailygrowth.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "oauth2")
@Getter
@Setter
public class OAuthProperties {
    @Data
    public static class Provider {
        private String restApiKey;
        private String redirectUri;
    }

    private Map<String, Provider> providers;
}
