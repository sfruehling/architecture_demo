package de.andrena.architecturedemo.web.security;

import de.andrena.architecturedemo.domain.grantedAuthorities.in.GrantedAuthoritiesUsecase;
import de.andrena.architecturedemo.domain.privilege.PrivilegeConstants;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocalPersistenceGrantedAuthoritiesExtractorTest {

    private final GrantedAuthoritiesUsecase mock = mock(GrantedAuthoritiesUsecase.class);
    private final LocalPersistenceGrantedAuthoritiesExtractor localPersistenceGrantedAuthoritiesExtractor =
            new LocalPersistenceGrantedAuthoritiesExtractor(mock);
    private final String TOKEN_USER = "user";

    @Test
    void readsTokens() {
        when(mock.getPrivilegesForUser(TOKEN_USER)).thenReturn(Collections.singletonList(PrivilegeConstants.PRIVILEGE_READ));

        Collection<GrantedAuthority> grantedAuthorities = localPersistenceGrantedAuthoritiesExtractor.convert(createJwtForName(TOKEN_USER));
        assertThat(grantedAuthorities, hasSize(1));
        assertThat(grantedAuthorities.iterator().next().getAuthority(), Matchers.is(PrivilegeConstants.PRIVILEGE_READ));
    }

    private Jwt createJwtForName(String name) {
        LocalDate issueddate = LocalDate.now();
        Instant issuedInstant = issueddate.atStartOfDay(ZoneId.of("Europe/Paris")).toInstant();
        LocalDate expiresdate = issueddate.plusDays(1);
        Instant expiresInstant = expiresdate.atStartOfDay(ZoneId.of("Europe/Paris")).toInstant();
        Map<String, Object> headers = new HashMap<>();
        headers.put("header1", "headerValue");
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.PRINCIPAL_CLAIM_NAME, name);

        return new Jwt("token", issuedInstant, expiresInstant, headers, claims);
    }
}
