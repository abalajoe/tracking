package com.iota.tracking.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    public static String SECRET_KEY;

    public AppConfig() {
    }

    @Value("${jwt.secret}")
    public void setSecretKey(String param) {
        SECRET_KEY = param;
    }
}


