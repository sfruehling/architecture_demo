package de.andrena.basepackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
	@Autowired
	private FindUserUsecase findUserUsecase;
	@Autowired
	private EditUserUsecase editUserUsecase;
	@Autowired
	private ResponseEntityFactory responseEntityFactory;
	@Autowired
	private ValidationProvider validationProvider;

	@GetMapping(value = Properties.USER_PATH)
	public ResponseEntity<?> getAllUsers() {
		List<UserDto> users = findUserUsecase.findAllUsers().stream().map(UserDto::fromBusinessObject).collect(Collectors.toList());
		return responseEntityFactory.createOkResponseForList(users, Properties.USER_PATH);
	}

	@GetMapping(value = Properties.USER_PATH + "/{domainId}")
	public ResponseEntity<?> getUserById(@PathVariable("domainId") String domainId) {
		UserWithRolesDto user = UserWithRolesDto.fromBusinessObject(findUserUsecase.findUserByDomainId(domainId));
		return responseEntityFactory.createOkResponse(user, Properties.USER_PATH + "/" + user.getId());
	}

	@PostMapping(value = Properties.USER_PATH)
	public ResponseEntity<?> createNewUser(@Valid @RequestBody UserWithRolesDto user, BindingResult result) {
		validationProvider.checkForValidationErrors(result);
		validationProvider.checkThatNoIdOrVersionAreGiven(user);
		User savedUser = editUserUsecase.saveNewUser(user.toBusinessObject());
		UserWithRolesDto savedUserDto = UserWithRolesDto.fromBusinessObject(savedUser);
		return responseEntityFactory.createCreatedResponse(savedUserDto, Properties.USER_PATH + "/" + savedUserDto.getId());
	}

	@PutMapping(value = Properties.USER_PATH + "/{domainId}")
	public ResponseEntity<?> updateUser(@PathVariable("domainId") String domainId, @Valid @RequestBody UserWithRolesDto user, BindingResult result) {
		validationProvider.checkForValidationErrors(result);
		validationProvider.checkThatVersionIsProvided(user);
		UserWithRolesDto updatedUser = UserWithRolesDto.fromBusinessObject(editUserUsecase.updateUser(domainId, user.toBusinessObject()));
		return responseEntityFactory.createOkResponse(updatedUser, Properties.USER_PATH + "/" + updatedUser.getId());
	}

	@DeleteMapping(value = Properties.USER_PATH + "/{domainId}")
	public void deleteUser(@PathVariable("domainId") String domainId) {
		editUserUsecase.deleteUserByDomainId(domainId);
	}

}
