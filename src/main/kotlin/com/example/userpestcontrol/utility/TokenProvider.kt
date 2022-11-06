package com.example.userpestcontrol.utility

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.JWTVerifier
import com.example.userpestcontrol.constant.SecurityConstant.AUTHORITIES
import com.example.userpestcontrol.constant.SecurityConstant.EXPIRATION_TIME
import com.example.userpestcontrol.constant.SecurityConstant.GET_ARRAYS_ADMINISTRATOR
import com.example.userpestcontrol.constant.SecurityConstant.GET_ARRAYS_LLC
import com.example.userpestcontrol.constant.SecurityConstant.TOKEN_CANNOT_BE_VERIFIED
import com.example.userpestcontrol.entity.EmployeePrincipal
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

@Component
class TokenProvider {
    @Value("\${jwt.secret}")
    private val secret: String? = null
    fun generateJwtToken(employeePrincipal: EmployeePrincipal): String {
        val claims = getClaimsFromUser(employeePrincipal)
        return JWT.create().withIssuer(GET_ARRAYS_LLC).withAudience(GET_ARRAYS_ADMINISTRATOR)
            .withIssuedAt(Date()).withSubject(employeePrincipal.username)
            .withArrayClaim(AUTHORITIES, claims).withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(secret!!.toByteArray()))
    }

    fun getAuthorities(token: String): List<GrantedAuthority> {
        val claims = getClaimsFromToken(token)
        return Arrays.stream(claims).map { role: String? -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())
    }

    fun getAuthentication(
        username: String?,
        authorities: List<GrantedAuthority?>?,
        request: HttpServletRequest?,
    ): Authentication {
        val userPasswordAuthToken = UsernamePasswordAuthenticationToken(username, null, authorities)
        userPasswordAuthToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        return userPasswordAuthToken
    }

    fun isTokenValid(username: String?, token: String): Boolean {
        val verifier = jWTVerifier
        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token)
    }

    fun getSubject(token: String?): String {
        val verifier = jWTVerifier
        return verifier.verify(token).subject
    }

    private fun isTokenExpired(verifier: JWTVerifier, token: String): Boolean {
        val expiration = verifier.verify(token).expiresAt
        return expiration.before(Date())
    }

    private fun getClaimsFromToken(token: String): Array<String> {
        val verifier = jWTVerifier
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String::class.java)
    }

    private val jWTVerifier: JWTVerifier
        private get() {
            val verifier: JWTVerifier = try {
                val algorithm = Algorithm.HMAC512(secret)
                JWT.require(algorithm).withIssuer(GET_ARRAYS_LLC).build()
            } catch (exception: JWTVerificationException) {
                throw JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED)
            }
            return verifier
        }

    private fun getClaimsFromUser(employeePrincipal: EmployeePrincipal): Array<String> {
        val authorities: MutableList<String> = ArrayList()
        for (grantedAuthority in employeePrincipal.authorities) {
            authorities.add(grantedAuthority.authority)
        }
        return authorities.toTypedArray()
    }
}