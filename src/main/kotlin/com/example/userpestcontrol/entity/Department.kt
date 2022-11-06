package com.example.userpestcontrol.entity

import org.hibernate.Hibernate
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.AUTO
import javax.persistence.Id

@Entity(name = "department")
data class Department(
    @Id
    @GeneratedValue(strategy = AUTO)
    val idDepartment: Long,
    val name: String
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Department

        return idDepartment == other.idDepartment
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(idDepartment = $idDepartment )"
    }
}
