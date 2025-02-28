package co.kr.cocomu.common.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AdminUserService {

    @Bean
    public UserDetailsService createSwaggerAdmin(
        @Value("${admin.swagger.username}") final String username,
        @Value("${admin.swagger.password}") final String password
    ) {
       return new InMemoryUserDetailsManager(
           User.withUsername(username)
            .password(passwordEncoder().encode(password))
            .roles("SWAGGER_ADMIN")
            .build()
       );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
