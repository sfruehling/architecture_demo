package de.andrena.architecturedemo.web;

import de.andrena.architecturedemo.web.http.HttpHeaderFactory;
import de.andrena.architecturedemo.web.http.ResponseEntityFactory;
import de.andrena.architecturedemo.web.validation.BindingResultsValidator;
import de.andrena.architecturedemo.web.validation.IdConsistencyValidator;
import de.andrena.architecturedemo.web.validation.ValidationProviderImpl;
import de.andrena.architecturedemo.web.validation.VersionAndIdValidator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ControllerTestConfig {
    @Bean
    public HttpHeaderFactory httpHeaderFactory() {
        return new HttpHeaderFactory();
    }

    @Bean
    public ResponseEntityFactory responseEntityFactory(HttpHeaderFactory httpHeaderFactory) {
        return new ResponseEntityFactory(httpHeaderFactory);
    }

    @Bean
    public ValidationProviderImpl validationProviderImpl() {
        return new ValidationProviderImpl();
    }

    @Bean
    public BindingResultsValidator bindingResultsValidator() {
        return new BindingResultsValidator();
    }

    @Bean
    public IdConsistencyValidator idConsistencyValidator() {
        return new IdConsistencyValidator();
    }

    @Bean
    public VersionAndIdValidator versionAndIdValidator() {
        return new VersionAndIdValidator();
    }
}
