package com.jinwoo.snsbackend_mainserver.global.security.config;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.global.security.component.JwtFilter;
import com.jinwoo.snsbackend_mainserver.global.security.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(  "/static/css/**, /static/js/**, *.ico");
        web.ignoring().antMatchers( "/v3/api-docs", "/configuration/ui", "/swagger-resources",
                "/configuration/security", "/swagger-ui.html", "/webjars/**","/swagger/**", "/api/auth/admin");

    }


    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .cors().disable()


                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()


                .authorizeRequests()
                    .antMatchers("/api/auth/login", "/api/auth/signup/**", "/api/auth/change/password",
                            "/api/auth/email/check", "/api/auth/reissue", "/swagger-ui/**",
                            "/swagger-resources/**").permitAll()
                    .antMatchers("/api/auth/code").hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()


                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

    }

}
