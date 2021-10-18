package feed.catalog.api.response.utils.validator.annotations.implementations;

import feed.catalog.api.response.utils.validator.annotations.ValidEnumValue;
import feed.catalog.api.response.utils.validator.annotations.interfaces.EnumValidator;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public class EnumValueValidator implements ConstraintValidator<ValidEnumValue, String> {

    private ValidEnumValue annotation;

    @Override
    public void initialize(ValidEnumValue constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String valueForValidation, ConstraintValidatorContext context) {
        boolean result = false;

        if (valueForValidation == null) {
            log.warn("Value for " + this.annotation.getClass().getTypeName() + " is null.");
            return true;
        }


        EnumValidator[] enumValues = this.annotation.enumClass().getEnumConstants();

        if (enumValues != null) {
            for (EnumValidator enumValue : enumValues) {
                if (valueForValidation.equals(enumValue.toString())) {
                    result = true;
                    break;
                }
                else {
                    String msg = Arrays.stream(enumValues).map(t -> t.toString())
                            .collect(Collectors.joining(","))
                            + " are the valid values for " + context.getDefaultConstraintMessageTemplate();

                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
                }
            }
        }
        return result;
    }
}
