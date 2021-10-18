package feed.catalog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feed.catalog.api.response.utils.validator.annotations.DTO;
import feed.catalog.api.response.utils.validator.annotations.implementations.DTOModelMapperImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class ModelViewConfig implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;

    private final List beanDTOAnnotated = new ArrayList();

    @Autowired
    public ModelViewConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().applicationContext(this.applicationContext).build();
        findAnnotatedClasses("feed.catalog.api.response.dto");
        argumentResolvers.add(new DTOModelMapperImpl(objectMapper,beanDTOAnnotated));
    }

    private ClassPathScanningCandidateComponentProvider createComponentScanner() {
        // Don't pull default filters (@Component, etc.):
        ClassPathScanningCandidateComponentProvider provider
                = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(DTO.class));
        return provider;
    }

    private void findAnnotatedClasses(String scanPackage) {
        ClassPathScanningCandidateComponentProvider provider = createComponentScanner();
        for (BeanDefinition beanDef : provider.findCandidateComponents(scanPackage)) {
            prepareBeanList(beanDef);
        }
    }

    private void prepareBeanList(BeanDefinition beanDef) {
        try {
            beanDTOAnnotated.add(Class.forName(beanDef.getBeanClassName()).newInstance());
        } catch (Exception e) {
            log.error("Got exception while preparing the Bean list: " + e.getMessage());
        }
    }
}
