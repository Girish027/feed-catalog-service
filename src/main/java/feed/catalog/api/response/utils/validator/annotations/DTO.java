package feed.catalog.api.response.utils.validator.annotations;

import feed.catalog.api.response.utils.validator.annotations.interfaces.DTOSymentic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.PARAMETER,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DTO {
    Class value() default DTOSymentic.class;
}
