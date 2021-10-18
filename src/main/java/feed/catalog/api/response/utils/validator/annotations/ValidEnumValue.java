package feed.catalog.api.response.utils.validator.annotations;

import feed.catalog.api.response.utils.validator.annotations.implementations.EnumValueValidator;
import feed.catalog.api.response.utils.validator.annotations.interfaces.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {EnumValueValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnumValue {


    public abstract String message() default "Feed.";

    public abstract Class<?>[] groups() default {};

    public abstract Class<? extends Payload>[] payload() default {};

    public abstract Class<? extends EnumValidator> enumClass();

    public abstract boolean ignoreCase() default false;
}
