package com.javainnovations.advancedweb.lecture3.example1.config;

import com.javainnovations.advancedweb.lecture3.example1.service.security.CustomReactiveUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
public class SecurityConfig {
//    @Bean
//    public ReactiveUserDetailsService userDetailsServiceReactive() {
//        var  u1 = User.withUsername("amer")
//                .password("123")
//                //.authorities("read")
//                .roles( "1", "2", "3")
//                .build();
//
//        var  u2 = User.withUsername("abdo")
//                .password("123")
//                .roles("2","3")
//                .build();
//
//        var  u3 = User.withUsername("Shadi")
//                .password("123")
//                .roles("3")
//                .build();
//
//        var uds = new MapReactiveUserDetailsService(u1, u2, u3);
//
//        return uds;
//    }

//    @Bean
//    public ReactiveUserDetailsService userDetailsService() {
//        return new CustomReactiveUserDetailsService();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChainBasicNotUsed(
            ServerHttpSecurity http) {

        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());

        http.authorizeExchange(
                c -> c

                        .pathMatchers("/admin/*").hasAuthority("1")
                        .pathMatchers("/customer/*").hasAuthority("2")
                        .anyExchange()
                        .permitAll()
        );

        return http.build();
    }

    //@Bean
    //    public PasswordEncoder passwordEncoder() {
    //        return new BCryptPasswordEncoder();
    //    }
}
