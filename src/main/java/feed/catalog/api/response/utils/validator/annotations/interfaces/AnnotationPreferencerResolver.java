package feed.catalog.api.response.utils.validator.annotations.interfaces;

import feed.catalog.api.response.utils.validator.annotations.DTO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.GroupSequence;

@GroupSequence({DTO.class, RequestBody.class})
public interface AnnotationPreferencerResolver {
}
