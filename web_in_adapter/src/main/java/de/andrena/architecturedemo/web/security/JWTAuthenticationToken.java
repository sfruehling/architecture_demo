package de.andrena.architecturedemo.web.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.net.MalformedURLException;
import java.util.Collection;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 7718994435182171505L;

	public JWTAuthenticationToken(Collection<? extends GrantedAuthority> authorities) throws MalformedURLException {
		super(authorities);
	}

	public void setAuthenticated() throws IllegalArgumentException {
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

}
