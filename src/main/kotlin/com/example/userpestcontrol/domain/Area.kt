package com.example.userpestcontrol.domain

import org.hibernate.Hibernate
import org.hibernate.annotations.Tables
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.AUTO
import javax.persistence.Id

@Entity(name = "area")
data class Area(
    @Id
    @GeneratedValue(strategy = AUTO)
    val idArea: Long,
    val name: String,
    val location: String,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Area

        return idArea == other.idArea
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(idArea = $idArea )"
    }
}