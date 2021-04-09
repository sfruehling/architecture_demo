package de.andrena.architecturedemo.web.security;

import de.andrena.architecturedemo.domain.grantedAuthorities.in.GrantedAuthoritiesUsecase;
import de.andrena.architecturedemo.domain.privilege.in.FindPrivilegesUsecase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class SecurityTestConfig {
    @Bean
    public LocalPersistenceGrantedAuthoritiesExtractor jwtAuthenticationConverter(GrantedAuthoritiesUsecase grantedAuthoritiesUsecase) {
        return new LocalPersistenceGrantedAuthoritiesExtractor(grantedAuthoritiesUsecase);
    }

    @Bean
    public GrantedAuthoritiesConverter jwtAuthenticationExtractor(LocalPersistenceGrantedAuthoritiesExtractor localPersistenceGrantedAuthoritiesExtractor) {
        return new GrantedAuthoritiesConverter(localPersistenceGrantedAuthoritiesExtractor);
    }

    @Bean
    public GrantedAuthoritiesUsecase grantedAuthoritiesUsecase() {
        return mock(GrantedAuthoritiesUsecase.class);
    }

    @Bean
    public FindPrivilegesUsecase findPrivilegesUsecase() {
        return mock(FindPrivilegesUsecase.class);
    }
    
}
