package es.sm2baleares.base.service.security;

public class SecurityConstants {


    static final String SECRET = "SecretKeyToGenJWTs";
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final long EXPIRATION_TIME = 21_600_000;


}
