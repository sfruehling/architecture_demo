package de.andrena.architecturedemo.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.validation.ValidationException;

@Component
public class BindingResultsValidator {

	public void checkForValidationErrors(BindingResult result) {
		if (result != null && result.hasErrors()) {
			throw new ValidationException("Feld '" + result.getFieldError().getField() + "': " + result.getFieldError().getDefaultMessage());
		}
	}
}
