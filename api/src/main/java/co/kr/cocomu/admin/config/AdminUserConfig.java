package co.kr.cocomu.admin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@RequiredArgsConstructor
public class AdminUserConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService createAdmin(
        @Value("${admin.username}") final String username,
        @Value("${admin.password}") final String password
    ) {
       return new InMemoryUserDetailsManager(
           User.withUsername(username)
            .password(passwordEncoder.encode(password))
            .roles(AdminConstants.ADMIN_ROLE)
            .build()
       );
    }

}
