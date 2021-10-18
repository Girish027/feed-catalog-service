package feed.catalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger {
    @Bean
    public Docket api() {


        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        List<Parameter> aParameters = new ArrayList<>();
        aParameterBuilder.name("secret_key").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        aParameters.add(aParameterBuilder.build());
        aParameterBuilder.name("access_token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        aParameters.add(aParameterBuilder.build());


        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("feed.catalog"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(aParameters);
    }
}
