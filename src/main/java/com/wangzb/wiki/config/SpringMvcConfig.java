package com.wangzb.wiki.config;
import com.wangzb.wiki.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    LoginInterceptor loginInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/test/**",
                        "/redis/**",
                        "/User/login",
                        "/Category/all",
                        "/Ebook/list",
                        "/Doc/all/**",
                        "/Doc/find-content/**",
                        "/Doc/vote/**",
                        "/ebook-snapshot/**"
                );


    }
}
