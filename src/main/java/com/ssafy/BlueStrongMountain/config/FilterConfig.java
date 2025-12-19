package com.ssafy.BlueStrongMountain.config;

import com.ssafy.BlueStrongMountain.auth.JwtAuthFilter;
import com.ssafy.BlueStrongMountain.auth.JwtProvider;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final JwtProvider jwtProvider;

    @Bean
    public FilterRegistrationBean<Filter> jwtAuthFilter(){
        FilterRegistrationBean<Filter> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthFilter(jwtProvider));
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
