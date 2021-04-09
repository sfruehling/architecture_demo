package de.andrena.architecturedemo.web.grantedauthorities;

import de.andrena.architecturedemo.domain.grantedAuthorities.in.GrantedAuthoritiesUsecase;
import de.andrena.architecturedemo.web.Properties;
import de.andrena.architecturedemo.web.http.ResponseEntityFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class GrantedAuthoritiesController {

	private final GrantedAuthoritiesUsecase grantedAuthoritiesUsecase;
	private final ResponseEntityFactory responseEntityFactory;

	@Autowired
	public GrantedAuthoritiesController(GrantedAuthoritiesUsecase grantedAuthoritiesUsecase,
										ResponseEntityFactory responseEntityFactory) {
		this.grantedAuthoritiesUsecase = grantedAuthoritiesUsecase;
		this.responseEntityFactory = responseEntityFactory;
	}

	@GetMapping(value = Properties.GRANTED_AUTHORITIES_PATH)
	public ResponseEntity<?> getGrantedAuthoritiesForLoggedInUser(Principal principal) {
		List<String>grantedAuthorities = grantedAuthoritiesUsecase.getPrivilegesForUser(principal.getName());
		return responseEntityFactory.createOkResponseForList(grantedAuthorities, Properties.GRANTED_AUTHORITIES_PATH);
	}

}
