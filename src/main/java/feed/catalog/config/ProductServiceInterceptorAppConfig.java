package feed.catalog.config;

import feed.catalog.services.ProductServiceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class ProductServiceInterceptorAppConfig implements WebMvcConfigurer
{
    @Autowired
    private ProductServiceInterceptor productServiceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(productServiceInterceptor).excludePathPatterns("/feed-service/health**","/health**","/swagger-resources/**","/webjars/**","/feed-service/swagger-resources/**","/feed-service/webjars/**","/swagger-ui.html**");;
    }
}
