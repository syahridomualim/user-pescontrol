package com.example.userpestcontrol.entity

import javax.persistence.*

@Entity
@Table(name = "presensi")
data class Presensi(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    val id: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns(
        JoinColumn(name = "id_employee", referencedColumnName = "id_employee"),
        JoinColumn(name = "first_name", referencedColumnName = "first_name"),
        JoinColumn(name = "area", referencedColumnName = "area")
    )
    val employee: Employee,

    val date: Long?,

    val timeIn: Long?,

    var timeOut: Long?,

    val note: String?,

    var locationIn: String?,

    var locationOut: String?,
)
