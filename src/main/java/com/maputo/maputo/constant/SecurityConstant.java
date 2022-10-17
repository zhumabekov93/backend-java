package com.maputo.maputo.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 432_000_000;//5 days in millis
    public static final String TOKEN_PREFIX="Bearer ";//means that whoever gave me this token I dont need to do any further verification
    public static final String JWT_TOKEN_HEADER="Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED="Token cannot be verified";
    public static final String MAPUTO_LLC="Maputo, LLC";
    public static final String MAPUTO_ADMINISTRATION="User management portal";
    public static final String AUTHORITIES="Authority";
    public static final String FORBIDDEN_MESSAGE="You need to be log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE="You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD="OPTIONS";
    public static final String[] PUBLIC_URLS={"/user/login", "/user/register","/user/resetpassword/**","/user/image/**"};
    //public static final String[] PUBLIC_URLS={"**"};
}
