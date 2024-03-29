package com.example.userpestcontrol.filter

import com.example.userpestcontrol.constant.SecurityConstant
import com.example.userpestcontrol.utility.TokenProvider
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.OK
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthorizationFilter(private val tokenProvider: TokenProvider) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (request.method.equals(SecurityConstant.OPTIONS_HTTP_METHOD, ignoreCase = true)) {
            response.status = OK.value()
        } else {
            val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
            if (authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstant.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response)
                return
            }

            val token = authorizationHeader.substring(SecurityConstant.TOKEN_PREFIX.length)
            val username = tokenProvider.getSubject(token)
            if (tokenProvider.isTokenValid(username, token) &&
                SecurityContextHolder.getContext().authentication == null
            ) {
                val authorities = tokenProvider.getAuthorities(token)
                val authentication = tokenProvider.getAuthentication(username, authorities, request)
                SecurityContextHolder.getContext().authentication = authentication
            } else {
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }

}