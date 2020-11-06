package com.commerce.backend.config;


import com.commerce.backend.constants.SwaggerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Autowired
    private SwaggerConstants swaggerConstants;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).host(swaggerConstants.getHostAddress()).pathMapping(swaggerConstants.getHostPath())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerConstants.getApiName())
                .description(swaggerConstants.getApiName())
                .contact(contact())
                .version(swaggerConstants.getApiVersion())
                .build();
    }

    private Contact contact() {
        return new Contact(swaggerConstants.getContactName(), swaggerConstants.getContactUrl(), swaggerConstants.getContactEmail());
    }
}
