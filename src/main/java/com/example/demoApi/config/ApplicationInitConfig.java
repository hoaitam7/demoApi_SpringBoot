package com.example.demoApi.config;

import com.example.demoApi.entity.User;
import com.example.demoApi.enums.Role;
import com.example.demoApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    ApplicationRunner applicationRunner (UserRepository userRepository){
        return application -> {
            if(userRepository.findByName("admin123456").isEmpty())
            {
                var role = new HashSet<String>();
                role.add(Role.ADMIN.name());
                User user = User.builder()
                        .name("admin123456")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .roles(role)
                        .build();
                userRepository.save(user);
                log.info("admin created with pass: admin");
            }
        };
    };
}
