package de.andrena.architecturedemo.web.security;

import de.andrena.architecturedemo.domain.privilege.PrivilegeConstants;
import de.andrena.architecturedemo.web.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Value("${security.oauth.jwks.url}")
    private String oAuthJwksUrl;

    @Autowired
    private GrantedAuthoritiesConverter grantedAuthoritiesConverter;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/actuator/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeRequests()//
                .antMatchers(HttpMethod.OPTIONS).permitAll()//
                .antMatchers(HttpMethod.GET, Properties.GRANTED_AUTHORITIES_PATH).authenticated() //
                .antMatchers(HttpMethod.GET).hasAuthority(PrivilegeConstants.PRIVILEGE_READ)//
                .antMatchers(HttpMethod.POST).hasAuthority(PrivilegeConstants.PRIVILEGE_WRITE)//
                .antMatchers(HttpMethod.PUT).hasAuthority(PrivilegeConstants.PRIVILEGE_WRITE)//
                .antMatchers(HttpMethod.DELETE).hasAuthority(PrivilegeConstants.PRIVILEGE_WRITE)//
                .anyRequest().authenticated().and()//
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(grantedAuthoritiesConverter);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // For Testing see MvcTestConfiugration!
        log.info("using oAuthJwksUrl: '" + oAuthJwksUrl + "'");
        return NimbusJwtDecoder.withJwkSetUri(oAuthJwksUrl).build();
    }
}
