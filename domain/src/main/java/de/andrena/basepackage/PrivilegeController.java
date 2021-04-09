package de.andrena.basepackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PrivilegeController {

	private final EditPrivilegesUsecase editPrivilegesUsecase;
	private final FindPrivilegesUsecase findPrivilegesUsecase;
	private final ResponseEntityFactory responseEntityFactory;
	private final ValidationProvider validationProvider;

	@Autowired
	public PrivilegeController(ResponseEntityFactory responseEntityFactory,
							   ValidationProvider validationProvider,
							   FindPrivilegesUsecase findPrivilegesUsecase,
							   EditPrivilegesUsecase editPrivilegesUsecase) {
		this.editPrivilegesUsecase = editPrivilegesUsecase;
		this.responseEntityFactory = responseEntityFactory;
		this.validationProvider = validationProvider;
		this.findPrivilegesUsecase = findPrivilegesUsecase;
	}

	@GetMapping(value = Properties.PRIVILEGE_PATH)
	public ResponseEntity<?> getAllPrivileges() {
		List<PrivilegeDto> privileges = findPrivilegesUsecase.findAllPrivileges().stream().map(PrivilegeDto::fromBusinessObject).collect(Collectors.toList());
		return responseEntityFactory.createOkResponseForList(privileges, Properties.PRIVILEGE_PATH);

	}

	@GetMapping(value = Properties.PRIVILEGE_PATH + "/{id}")
	public ResponseEntity<?> getPrivilegeById(@PathVariable("id") String domainId) {
		PrivilegeDto privilege = PrivilegeDto.fromBusinessObject(findPrivilegesUsecase.findPrivilegeByDomainId(domainId));
		return responseEntityFactory.createOkResponse(privilege, Properties.PRIVILEGE_PATH + "/" + privilege.getId());
	}

	@PostMapping(value = Properties.PRIVILEGE_PATH)
	public ResponseEntity<?> createNewPrivilege(@Valid @RequestBody PrivilegeDto privilege, BindingResult result) {
		validationProvider.checkForValidationErrors(result);
		validationProvider.checkThatNoIdOrVersionAreGiven(privilege);
		Privilege savedPrivilege = editPrivilegesUsecase.saveNewPrivilege(privilege.toBusinessObject());
		return responseEntityFactory.createCreatedResponse(PrivilegeDto.fromBusinessObject(savedPrivilege), Properties.PRIVILEGE_PATH + "/" + savedPrivilege.getId());
	}

	@PutMapping(value = Properties.PRIVILEGE_PATH + "/{id}")
	public ResponseEntity<?> updatePrivilege(@PathVariable("id") String domainId, @Valid @RequestBody PrivilegeDto privilege, BindingResult result) {
		validationProvider.checkForValidationErrors(result);
		validationProvider.checkThatVersionIsProvided(privilege);
		Privilege updatedPrivilege = editPrivilegesUsecase.updatePrivilege(domainId, privilege.toBusinessObject());
		return responseEntityFactory.createOkResponse(PrivilegeDto.fromBusinessObject(updatedPrivilege), Properties.PRIVILEGE_PATH + "/" + updatedPrivilege.getId());
	}

	@DeleteMapping(value = Properties.PRIVILEGE_PATH + "/{id}")
	public void deletePrivilegeById(@PathVariable("id") String domainId) {
		editPrivilegesUsecase.deletePrivilegeByDomainId(domainId);
	}

}
