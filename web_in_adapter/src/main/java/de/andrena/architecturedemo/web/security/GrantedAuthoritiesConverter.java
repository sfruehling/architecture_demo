package de.andrena.architecturedemo.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Service;

@Service
public class GrantedAuthoritiesConverter extends JwtAuthenticationConverter {


    @Autowired
    public GrantedAuthoritiesConverter(LocalPersistenceGrantedAuthoritiesExtractor localPersistenceGrantedAuthoritiesExtractor) {
        setJwtGrantedAuthoritiesConverter(localPersistenceGrantedAuthoritiesExtractor);
        setPrincipalClaimName(SecurityConstants.PRINCIPAL_CLAIM_NAME);
    }

}
