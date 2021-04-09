package de.andrena.basepackage;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@EnableWebMvc
@ControllerAdvice
public class ExceptionHandling {

	@ResponseBody
	@ExceptionHandler({ValidationException.class,
			NoVersionProvidedException.class,
			NoIdAllowedException.class,
			NoVersionAllowedException.class,
			DatabaseException.class,
			HttpMessageNotReadableException.class,
			IdInconsistentException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	Problem validationExceptionHandler(Exception ex) {
		return createProblemFromException(ex);
	}

	@ResponseBody
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	Problem ConstraintViolationExceptionHandler(Exception ex) {
		return createProblemFromStrings(ex, "Entity cannot be deleted because it is still referred to.");
	}


	private Problem createProblemFromException(Exception ex) {
		Map<String, String> s = new HashMap<>();
		s.put("logref", ex.getClass().getSimpleName());
		s.put("message", ex.getMessage());
		return Problem.create().withProperties(s);
	}

	private Problem createProblemFromStrings(Exception ex, String message) {
		Map<String, String> s = new HashMap<>();
		s.put("logref", ex.getClass().getSimpleName());
		s.put("message", message);
		return Problem.create().withProperties(s);
	}
}
