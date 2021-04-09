package de.andrena.basepackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class RoleController {

	@Autowired
	private FindRoleUsecase findRoleUsecase;
	@Autowired
	private EditRoleUsecase editRoleUsecase;
	@Autowired
	private ResponseEntityFactory responseEntityFactory;
	@Autowired
	private ValidationProvider validationProvider;

	@GetMapping(value = Properties.ROLE_PATH)
	public ResponseEntity<?> getAllRoles() {
		List<RoleDto> roles = findRoleUsecase.findAllRoles().stream().map(role -> RoleDto.fromBusinessObject(role)).collect(Collectors.toList());
		return responseEntityFactory.createOkResponseForList(roles, Properties.ROLE_PATH);
	}

	@GetMapping(value = Properties.ROLE_PATH + "/{id}")
	public ResponseEntity<?> getRoleById(@PathVariable("id") String id) {
		RoleWithPrivilegesDto role = RoleWithPrivilegesDto.fromBusinessObject(findRoleUsecase.findRoleById(id));
		return responseEntityFactory.createOkResponse(role, Properties.ROLE_PATH + "/" + role.getId());

	}

	@PostMapping(value = Properties.ROLE_PATH)
	public ResponseEntity<?> createNewRole(@Valid @RequestBody RoleWithPrivilegesDto roleDto, BindingResult result) {
		validationProvider.checkForValidationErrors(result);
		validationProvider.checkThatNoIdOrVersionAreGiven(roleDto);
		RoleWithPrivilegesDto savedRole = RoleWithPrivilegesDto.fromBusinessObject(editRoleUsecase.saveNewRole(roleDto.toBusinessObject()));
		return responseEntityFactory.createCreatedResponse(savedRole, Properties.ROLE_PATH + "/" + roleDto.getId());
	}

	@PutMapping(value = Properties.ROLE_PATH + "/{id}")
	public ResponseEntity<?> updateRole(@PathVariable("id") String id, @Valid @RequestBody RoleWithPrivilegesDto roleDto, BindingResult result) {
		validationProvider.checkIdConsistency(id, roleDto.getId());
		validationProvider.checkForValidationErrors(result);
		validationProvider.checkThatVersionIsProvided(roleDto);
		RoleWithPrivilegesDto updatedRole = RoleWithPrivilegesDto.fromBusinessObject(editRoleUsecase.update(roleDto.toBusinessObject()));
		return responseEntityFactory.createOkResponse(updatedRole, Properties.ROLE_PATH + "/" + roleDto.getId());
	}

	@DeleteMapping(value = Properties.ROLE_PATH + "/{id}")
	public void deleteRoleById(@PathVariable("id") String id) {
		editRoleUsecase.deleteRoleById(id);
	}

}
