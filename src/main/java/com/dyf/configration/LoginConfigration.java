package com.dyf.configration;

import com.dyf.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfigration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        final String[] adds = new String[]{
                "/"
        };
        String[] excludes = new String[]{
                "/",
                "/error",
                "/seller/logout",
                "/admin/login",
                "/api/*"};
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns(adds)
                .excludePathPatterns(excludes);
    }

}
