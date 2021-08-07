package com.revo.eshop.userservice.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 432_000_000; // 5 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String ESHOP = "ESHOP";
    public static final String ESHOP_PORTAL = "Eshop Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
//    public static final String[] PUBLIC_URLS = { "/users/login", "/users/register","/users/status/check"};
    public static final String[] PUBLIC_URLS = { "**" };
}
