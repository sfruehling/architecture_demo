package de.andrena.architecturedemo.web.privilege;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

class PrivilegeDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void allowsCorrectDto() {
        PrivilegeDto privilegeDto =  PrivilegeDto.builder().name("name").description("description").build();
        Set<ConstraintViolation<PrivilegeDto>> violations = validator.validate(privilegeDto);
        assertThat(violations, hasSize(0));
    }

    @Test
    void validatesCorrectlyNullOrEmpty() {
        PrivilegeDto privilegeDto =  PrivilegeDto.builder().build();
        Set<ConstraintViolation<PrivilegeDto>> violations = validator.validate(privilegeDto);
        assertThat(violations, hasSize(4));
        assertThat(violations, containsViolationForAttributeWithMessageTemplate("name","{javax.validation.constraints.NotNull.message}"));
        assertThat(violations, containsViolationForAttributeWithMessageTemplate("name","{javax.validation.constraints.NotEmpty.message}"));
        assertThat(violations, containsViolationForAttributeWithMessageTemplate("description","{javax.validation.constraints.NotNull.message}"));
        assertThat(violations, containsViolationForAttributeWithMessageTemplate("description","{javax.validation.constraints.NotEmpty.message}"));
    }

    private Matcher<Set<ConstraintViolation<PrivilegeDto>>> containsViolationForAttributeWithMessageTemplate(String name, String message) {
        return new TypeSafeMatcher<Set<ConstraintViolation<PrivilegeDto>>>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("Violations should contain " +
                        "one with MessageTemplate: " + message + " " +
                        "and propertypath="+name);
            }

            @Override
            protected boolean matchesSafely(Set<ConstraintViolation<PrivilegeDto>> constraintViolations) {
                return constraintViolations.stream()
                        .anyMatch(v -> v.getMessageTemplate().equals(message)
                                && v.getPropertyPath().toString().equals(name));
            }
        };
    }
}
