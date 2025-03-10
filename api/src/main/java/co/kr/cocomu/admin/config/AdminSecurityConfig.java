package co.kr.cocomu.admin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class AdminSecurityConfig {

    private final UserDetailsService adminUserDetailsService;

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher(AdminConstants.getAllAdminUris())
            .authorizeHttpRequests(auth -> AdminUriGroup.getAdminUriGroups()
                .forEach(group -> auth.requestMatchers(group.getUrisArray()).hasRole(group.requiredRole())
                ))
            .httpBasic(Customizer.withDefaults())
            .userDetailsService(adminUserDetailsService)
            .csrf(AbstractHttpConfigurer::disable)
            .build();
    }

}
