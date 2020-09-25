package org.waterlevelmonitor.backend.auth

import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@EnableWebSecurity
class WebSecurityConfig(
        private val bCryptPasswordEncoder: BCryptPasswordEncoder,
        private val userDetailsService: UserDetailsServiceImpl
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {

        http?.let {
            http.cors().and().csrf().disable().authorizeRequests()
                    .antMatchers("/rivers", "/locations", "/login").permitAll()
                    .antMatchers(HttpMethod.POST, "/users/signup").permitAll()
                    .anyRequest().authenticated()
                    .and().addFilter(AuthenticationFilter(authenticationManager()))
                    .addFilter(AuthorizationFilter(authenticationManager()))
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
    }

    @Throws(Exception::class)
    override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder)
    }
}