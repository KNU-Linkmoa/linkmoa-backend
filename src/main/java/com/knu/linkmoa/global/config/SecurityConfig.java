package com.knu.linkmoa.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knu.linkmoa.auth.itself.filter.CustomJsonUserPasswordAuthenticationFilter;
import com.knu.linkmoa.auth.itself.handler.LoginFailureHandler;
import com.knu.linkmoa.auth.itself.handler.LoginSuccessHandler;
import com.knu.linkmoa.auth.itself.service.LoginService;
import com.knu.linkmoa.auth.jwt.filter.JwtAuthorizationFilter;
import com.knu.linkmoa.auth.jwt.filter.JwtExceptionHandlerFilter;
import com.knu.linkmoa.auth.oauth2.handler.CustomOauth2SuccessHandler;
import com.knu.linkmoa.auth.oauth2.service.CustomOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtExceptionHandlerFilter jwtExceptionHandlerFilter;
    private final CustomOauth2UserService customOauth2UserService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final CustomOauth2SuccessHandler customOauth2SuccessHandler;
    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return
                http
                        .cors(corsCustomizer ->corsCustomizer.configurationSource(corsConfigurationSource()))
                        .csrf(AbstractHttpConfigurer::disable)
                        .formLogin(AbstractHttpConfigurer::disable)
                        .httpBasic(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests((auth) -> auth
                                .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/members/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/v3/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/jwt/reissue")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                                .anyRequest().authenticated()
                        )
                        .oauth2Login((oauth2) -> oauth2
                                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOauth2UserService))
                                .successHandler(customOauth2SuccessHandler))
                        .addFilterAfter(customJsonUserPasswordAuthenticationFilter(), LogoutFilter.class)
                        .addFilterBefore(jwtAuthorizationFilter, CustomJsonUserPasswordAuthenticationFilter.class)
                        .addFilterBefore(jwtExceptionHandlerFilter, JwtAuthorizationFilter.class)
                        .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .build();
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider
                = new DaoAuthenticationProvider(passwordEncoder);

        provider.setUserDetailsService(loginService);

        return new ProviderManager(provider);
    }
    @Bean
    public CustomJsonUserPasswordAuthenticationFilter customJsonUserPasswordAuthenticationFilter() {
        CustomJsonUserPasswordAuthenticationFilter customJsonUserPasswordAuthenticationFilter
                = new CustomJsonUserPasswordAuthenticationFilter(objectMapper);

        customJsonUserPasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customJsonUserPasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        customJsonUserPasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler);

        return customJsonUserPasswordAuthenticationFilter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("*"));

        config.setMaxAge(3600L); //1시간
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

}
