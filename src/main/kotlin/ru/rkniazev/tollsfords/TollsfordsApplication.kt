package ru.rkniazev.tollsfords

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.MultipartConfigFactory
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.util.unit.DataSize
import javax.servlet.MultipartConfigElement

@EnableJpaAuditing
@SpringBootApplication
class TollsfordsApplication

@Bean
fun multipartConfigElement(): MultipartConfigElement? {
	val factory = MultipartConfigFactory()
	factory.setMaxFileSize(DataSize.ofKilobytes(128))
	factory.setMaxRequestSize(DataSize.ofKilobytes(128))
	return factory.createMultipartConfig()
}

fun main(args: Array<String>) {
	runApplication<TollsfordsApplication>(*args)
}
