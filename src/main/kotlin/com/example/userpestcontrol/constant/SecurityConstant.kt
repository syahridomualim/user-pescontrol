package com.example.userpestcontrol.constant

object SecurityConstant {

    const val EXPIRATION_TIME = 432_000_000L // 5 days expressed in milliseconds
    const val TOKEN_PREFIX = "Bearer "
    const val JWT_TOKEN_HEADER = "Jwt-Token"
    const val TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified"
    const val GET_ARRAYS_LLC = "Get Arrays, LLC"
    const val GET_ARRAYS_ADMINISTRATOR = "User Management Portal"
    const val AUTHORITIES = "authorities"
    const val FORBIDDEN_MESSAGES = "You need to log in to access the page"
    const val ACCESS_DENIED_MESSAGES = "You don't have permission to this page"
    const val OPTIONS_HTTP_METHOD = "OPTION"
    val PUBLIC_URLS = arrayOf("/user/login", "/user/register", "/user/resetpassword/**", "/user/image/**")
//    val PUBLIC_URL: Array<String> = arrayOf("**")
}