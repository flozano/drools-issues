package drools.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import drools.issues.executor.RuntimeType;
import drools.issues.model.Person;

/**
 * @see https://issues.redhat.com/browse/DROOLS-7195
 */
class ExecutionTest extends AbstractTest {

	@ParameterizedTest
	@EnumSource(RuntimeType.class)
	public void firstLevelProperty(RuntimeType runtimeType) {
		try (var executor = executor(runtimeType, "ExecutionTest_firstLevelProperty_mvel.drl")) {
			var fact = emmanuel();
			var result = executor.fire(fact);
			assertEquals(1, result.getNumberOfFiredRules());
			assertEquals(45, fact.getAge());
		}
	}

	@ParameterizedTest
	@EnumSource(RuntimeType.class)
	public void nestedProperty(RuntimeType runtimeType) {
		try (var executor = executor(runtimeType, "ExecutionTest_nestedProperty_mvel.drl")) {
			var fact = emmanuel();
			var result = executor.fire(fact);
			assertEquals(1, result.getNumberOfFiredRules());
			assertEquals("FR", fact.getAddress().getCountry());
		}
	}

	@ParameterizedTest
	@EnumSource(RuntimeType.class)
	public void nestedPropertyMixed(RuntimeType runtimeType) {
		try (var executor = executor(runtimeType, "ExecutionTest_nestedProperty_mixed.drl")) {
			var fact = emmanuel();
			var result = executor.fire(fact);
			assertEquals(1, result.getNumberOfFiredRules());
			assertEquals("FR", fact.getAddress().getCountry());
			assertEquals("Champs Elysees", fact.getAddress().getStreet());
		}
	}

	@ParameterizedTest
	@EnumSource(RuntimeType.class)
	public void useGetter(RuntimeType runtimeType) {
		try (var executor = executor(runtimeType, "ExecutionTest_nestedProperty_getter.drl")) {
			var fact = emmanuel();
			var result = executor.fire(fact);
			assertEquals(1, result.getNumberOfFiredRules());
			assertEquals("FR", fact.getAddress().getCountry());
			assertEquals("Champs Elysees", fact.getAddress().getStreet());
		}
	}

	private static Person emmanuel() {
		var fact = TestData.person();
		fact.setFirstName("Emmanuel");
		fact.setLastName("Macron");
		return fact;
	}


}
