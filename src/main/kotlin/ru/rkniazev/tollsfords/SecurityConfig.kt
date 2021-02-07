package ru.rkniazev.tollsfords

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser("admin")
            .password(encoder().encode("pass"))
            .roles( "ADMIN")
//            .and()
//            .withUser("test")
//            .password(encoder().encode("testpass"))
//            .roles("test")
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.DELETE, "/shop/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/shop/**").hasAnyRole("ADMIN")
            .antMatchers(HttpMethod.POST, "/shop/**").hasAnyRole("ADMIN")
            .antMatchers(HttpMethod.GET, "/parsing/**").hasAnyRole("ADMIN")
            .antMatchers(HttpMethod.POST, "/parsing/**").hasAnyRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/parsing/**").hasAnyRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/parsing/**").hasAnyRole("ADMIN")
            .and()
            .csrf().disable()
            .formLogin().disable()
    }

}