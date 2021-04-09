package de.andrena.architecturedemo.web.security;

import de.andrena.architecturedemo.domain.grantedAuthorities.in.GrantedAuthoritiesUsecase;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class LocalPersistenceGrantedAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {

    private GrantedAuthoritiesUsecase grantedAuthoritiesUsecase;

    @Autowired
    public LocalPersistenceGrantedAuthoritiesExtractor(GrantedAuthoritiesUsecase grantedAuthoritiesUsecase) {
        this.grantedAuthoritiesUsecase = grantedAuthoritiesUsecase;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        String userMNumber = extractUserMNumber(jwt);

        List<GrantedAuthority> authorities = grantedAuthoritiesUsecase.getPrivilegesForUser(userMNumber).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        logAuthorities(userMNumber, authorities);
        return authorities;
    }

    private void logAuthorities(String userMNumber, List<GrantedAuthority> authorities) {
        if (log.isDebugEnabled()) {
            String listed = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
            log.debug("user: '{}' granted_authorities: '{}'", userMNumber, listed);
        }
    }

    private String extractUserMNumber(Jwt jwt) {
        return (String) jwt.getClaims().get(SecurityConstants.PRINCIPAL_CLAIM_NAME);
    }
}
