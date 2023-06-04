package com.hyunn.carrot.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomOAuth2UserService customOAuth2User;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll() // 허가
                .antMatchers("/login").permitAll() // 허가
                .antMatchers("/login_normal").permitAll() // 허가
                .antMatchers("/signup").permitAll()
                .antMatchers("/**").hasRole("USER")  // /user 경로의 하위 경로는 ROLE_USER 역할을 가진 사용자에게만 허용
                // .antMatchers("/**").hasRole("ADMIN")  // 모든 경로는 ROLE_ADMIN 역할을 가진 사용자에게만 허용
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied")
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessUrl("/").permitAll()
                .and()
                .oauth2Login().loginPage("/")
                .userInfoEndpoint()
                .userService(customOAuth2User);
    }
}

