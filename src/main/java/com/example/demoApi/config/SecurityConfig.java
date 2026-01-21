package com.example.demoApi.config;

import com.example.demoApi.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
//cấu hình security
public class SecurityConfig {

    private final String [] PUBLIC_URLS = {
            "api/users",
            "api/auth/login",
            "api/auth/introspect",
    };
    @Value("${jwt.signerKey}") /// file application.properties
    private String jwtSignerKey;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
///        http.authorizeHttpRequests(auth -> auth
///           .requestMatchers("/public/**").permitAll()
///            .requestMatchers("/admin/**").hasRole("ADMIN")
///           .anyRequest().authenticated()
///       );
        httpSecurity.authorizeHttpRequests(requests ->
                requests.requestMatchers(HttpMethod.POST, PUBLIC_URLS).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole(Role.ADMIN.name())
                        //các requestMatchers
                        /// ....
                        .anyRequest().authenticated());

        // Cấu hình OAuth2 Resource Server để xác thực JWT
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
        );
        /// cách 1:
             // Tắt CSRF vì hệ thống không sử dụng cookie để xác thực.
            // và sử dụng JWT trong tiêu đề Authorization thay vì cookie.
            // Các cuộc tấn công CSRF dựa vào việc trình duyệt tự động gửi cookie.
        httpSecurity.csrf(AbstractHttpConfigurer::disable); /// chỉ vô hiệu khi cần (ví dụ API)
        /// cách 2: tắt csrf
        // httpSecurity.csrf(csrf -> csrf.disable());

        return httpSecurity.build();
    }


    //converter
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        // 3. Sử dụng HmacSHA256 để phù hợp với chuẩn JWT HMAC
        SecretKeySpec secretKeySpec = new SecretKeySpec(jwtSignerKey.getBytes(), "HmacSHA256");
        // 4. Thêm .build() để hoàn tất tạo JwtDecoder
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}