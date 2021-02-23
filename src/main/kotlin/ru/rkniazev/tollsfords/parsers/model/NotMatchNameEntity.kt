package ru.rkniazev.tollsfords.parsers.model

import javax.persistence.*

@Entity
@Table(name = "not_match_name")
class NotMatchNameEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true)
    val name: String = ""
)