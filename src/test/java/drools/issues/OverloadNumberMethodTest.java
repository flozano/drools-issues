package drools.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import drools.issues.executor.RuntimeType;
import drools.issues.model.Person;

public class OverloadNumberMethodTest extends AbstractTest {

	/**
	 * @see https://issues.redhat.com/browse/DROOLS-7196
	 */
	@ParameterizedTest
	@EnumSource(RuntimeType.class)
	public void arrayOfIntToLongs(RuntimeType runtimeType) {
		try (var executor = executor(runtimeType, "OverloadNumberMethodTest_arrayOfIntToLongs.drl")) {
			var fact = fact();
			var result = executor.fire(fact);
			assertEquals(1, result.getNumberOfFiredRules());
			assertEquals(Set.of(7l, 77l, 777l), fact.getFavoriteNumbers());
		}
	}

	private static Person fact() {
		var fact = TestData.person();
		fact.setFirstName("Pepe");
		return fact;
	}
}
