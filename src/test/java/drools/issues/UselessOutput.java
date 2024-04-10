package drools.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import drools.issues.model.Person;
import drools.issues.model.RegardsManager;
import drools.issues.model.SendRegards;

class UselessOutput extends AbstractTest {


	@ParameterizedTest
	@EnumSource(TestScenario.class)
	public void testUsefulFactIsRetrieved(TestScenario scenario) {
		try (var executor = executor(scenario, "UselessOutput_testUseful.drl")) {
			var fact = donald();
			var result = executor.fire(fact);
			assertEquals(2, result.getNumberOfFiredRules());
			assertEquals(2, result.getOutput().size());
			var sendRegards = result.getSingleOutput(SendRegards.class);
			assertNotNull(sendRegards);
			assertEquals("Hi Donald!", sendRegards.getMessage());
		}
	}

	@ParameterizedTest
	@EnumSource(TestScenario.class)
	public void testUselessFactIsRetrieved(TestScenario scenario) {
		try (var executor = executor(scenario, "UselessOutput_testUseless.drl")) {
			var fact = donald();
			var result = executor.fire(fact);
			assertEquals(1, result.getNumberOfFiredRules());
			assertEquals(2, result.getOutput().size());
			var sendRegards = result.getSingleOutput(SendRegards.class);
			assertNotNull(sendRegards);
			assertEquals("Hi Donald!", sendRegards.getMessage());
		}
	}

	private static Person donald() {
		var fact = TestData.person();
		fact.setFirstName("Donald");
		fact.setLastName("Duck");
		fact.setAge(76);
		return fact;
	}

}
