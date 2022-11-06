package com.example.userpestcontrol.entity

import org.hibernate.Hibernate
import java.io.Serializable
import javax.persistence.*
import javax.persistence.GenerationType.AUTO

@Entity(name = "employee")
data class Employee(
    @Id
    @GeneratedValue(strategy = AUTO)
    val id: Long? = null,

    @Column(name = "id_employee")
    val idEmployee: Long,
    @Column(name = "first_name")
    var firstName: String?,
    var lastName: String?,
    var password: String?,
    var email: String?,
    var profileImageUrl: String?,
    var activeDate: Long?,
    var role: String?,
    var authorities: Array<out String>?,
    var lastLoginDate: Long? = null,
    var lastLoginDisplayDate: Long? = null,
    var isActive: Boolean = true,
    var isNotLocked: Boolean = true,

    @OneToOne
    @JoinColumn(name = "area", referencedColumnName = "name", nullable = true)
    var area: Area? = null,

    @OneToOne
    @JoinColumn(name = "department", referencedColumnName = "name", nullable = true)
    var department: Department? = null,

    ) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Employee

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , idEmployee = $idEmployee , firstName = $firstName , lastName = $lastName , password = $password , email = $email , profileImageUrl = $profileImageUrl , activeDate = $activeDate , role = $role , authorities = $authorities , lastLoginDate = $lastLoginDate , lastLoginDisplayDate = $lastLoginDisplayDate , isActive = $isActive , isNotLocked = $isNotLocked , area = $area , department = $department )"
    }
}