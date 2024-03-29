package com.piotrwalkusz.lebrb.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                        .antMatchers('/css/**', 'js/**', '/wordscounter/**', '/index').permitAll()
        http.formLogin().permitAll()
        http.logout().permitAll()
        http.csrf().disable()
    }
}
