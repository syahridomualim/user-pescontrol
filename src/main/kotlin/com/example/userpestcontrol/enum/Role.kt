package com.example.userpestcontrol.enum

import com.example.userpestcontrol.constant.Authority.COORDINATOR_AUTHORITIES
import com.example.userpestcontrol.constant.Authority.SUPER_ADMIN_AUTHORITIES
import com.example.userpestcontrol.constant.Authority.USER_AUTHORITIES

enum class Role(private vararg val authorities: String) {

    ROLE_USER(*USER_AUTHORITIES),
    ROLE_ADMIN(*SUPER_ADMIN_AUTHORITIES),
    ROLE_COORDINATOR(*COORDINATOR_AUTHORITIES);

    fun getAuthorities(): Array<out String> = authorities
}