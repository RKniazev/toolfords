package ru.rkniazev.tollsfords.parsers.model

import org.springframework.data.jpa.repository.JpaRepository

interface NotMatchNameRepository : JpaRepository<NotMatchNameEntity, Long> {
    fun findByName(name: String): Iterable<NotMatchNameEntity>
}