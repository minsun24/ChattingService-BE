package com.example.chatserver.common.configs;

import com.example.chatserver.common.auth.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfigs {

    private final JwtAuthFilter jwtAuthFilter;

    // 에외 처리 객체 주입
    private final CustomAuthentication customAuthentication;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity httpSecurity, CustomAuthentication customAuthentication) throws Exception {
        
        // 커스텀한 SecurityFilterChain (스프링 필터)를 반환
        // Spring에서 필터를 싱글톤 객체로 등록되어 사용자 인증 정보 검토
        return httpSecurity
                // 필터에 CORS 설정
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)   // csrf 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 비활성화

                // 특정 URL 패턴에 대해서는 Authentication 객체를 요구하지 않음 (인증 처리 제외)
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/api/members/signup", "/api/members/login").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())  // 그 외 요청은 모두 인증 필요

                // 세션 방식을 사용하지 않겠다.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 인증 & 인가 예외 처리 등록
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthentication) // 인증 실패 시
                        .accessDeniedHandler(customAccessDeniedHandler) // 인가 실패 시
                )

                // 토큰 검증 : 사용자 요청이 token 을 포함시켜 오면, 해당 필터가 서버에서 만들어진 것인지 검증
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*"));  // 모든 HTTP 메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤딩값 허용
        configuration.setAllowCredentials(true);       // 자격 증명 허용
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);     // 모든 url에 패턴에 대해 cors 허용 설정

        return source;
    }

    // 패스워드 암호화
    @Bean
    public PasswordEncoder encodePassword(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}

/*
    <싱글톤 객체를 만드는 2가지 방법>

    1. @Compnent 가 붙어 있는 어노테이션을 가져다가 사용하는 방법
        ex) @Controller, @Service 어노테이션을 사용하면, 해당 어노테이션이 @Component를 포함하고 있으므로,
        싱글톤으로 사용할 수 있다.
    2. 메서드 위에 @Bean, 클래스에 @Configuration 을 설정해주는 방법
        => @Bean 이라는 메서드가 "리턴"해주는 객체를 "싱글톤"으로 만들겠다는 뜻.

 */

/*
    => 토큰 기반 인증 방식을 사용하 경우 비활성화 해줘도 되는 보안 인증 방식

    CSRF == 보안 공격
       .csrf(AbstractHttpConfigurer::disable)
    -> 보안 공격에 대해 대비하지 않겠다.
    -> 비즈니스 로직에서 충분히 방어할 수 있는 방법이 있기 때문에
    -> 시큐리티 필터에 설정하는 대신, 로직 내에서 방어할 것

    HTTP Basic
    - 보안 인증 방식이지만 잘 사용하지 않음.

 */

/*
 로그인 방식
 1. 세션 방식
 2. 토큰 방식

 */
