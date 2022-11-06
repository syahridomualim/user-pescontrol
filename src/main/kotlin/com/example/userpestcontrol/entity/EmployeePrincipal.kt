package com.example.userpestcontrol.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Arrays.stream
import java.util.stream.Collectors

class EmployeePrincipal(private val employee: Employee?) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return stream(employee?.authorities).map { role: String -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())
    }
    override fun getPassword(): String? {
        return employee?.password
    }

    override fun getUsername(): String? {
        return employee?.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return employee?.isNotLocked!!
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return employee?.isActive!!
    }
}