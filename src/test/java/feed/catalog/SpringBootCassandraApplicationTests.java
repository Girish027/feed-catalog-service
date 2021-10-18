package feed.catalog;

import feed.catalog.api.response.dto.FeedCreateDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class SpringBootCassandraApplicationTests {
	@Mock
	static SpringBootCassandraApplication s;

	@Test
	public void contextLoads() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		FeedCreateDTO feed = new FeedCreateDTO();
		Set<ConstraintViolation<FeedCreateDTO>> constraintViolations =
				validator.validate(feed);

		if (constraintViolations.size() > 0) {
			constraintViolations.stream().forEach(
					SpringBootCassandraApplicationTests::printError);
		} else {
			//log.info("Feed : ",feed);
		}
	}

	@Test
	public void main(){
		//String[] args = new String[0];
		//assertThrows(org.springframework.context.ApplicationContextException.class,()->
		//		SpringBootCassandraApplication.main()) ;
	}

	private static void printError(ConstraintViolation<FeedCreateDTO> violation) {
		//log.error(violation.getPropertyPath() + " " + violation.getMessage());
	}

}
