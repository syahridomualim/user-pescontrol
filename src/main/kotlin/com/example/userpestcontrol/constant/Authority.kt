package com.example.userpestcontrol.constant


object Authority {
    val USER_AUTHORITIES = arrayOf("user:read", "user:create", "user:update")
    val COORDINATOR_AUTHORITIES = arrayOf("user:read", "user:update")
    val SUPER_ADMIN_AUTHORITIES = arrayOf("user:read", "user:create", "user:update", "user:delete")
}
