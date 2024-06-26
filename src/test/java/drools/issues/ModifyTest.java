package drools.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import drools.issues.executor.InvalidRulesException;
import drools.issues.executor.RulesExecutor;
import drools.issues.executor.RuntimeType;

class ModifyTest extends AbstractTest {

	/**
	 * @see https://issues.redhat.com/browse/DROOLS-7195
	 */
	@ParameterizedTest
	@EnumSource(TestScenario.class)
	void maxNumberOfRules_getterSyntax(TestScenario scenario) {
		var fact = TestData.person();

		// OK with both mvel and executable model
		try (var executor = executor(scenario, "ModifyTest_maxNumberOfRules_getter.drl")) {
			var result = executor.fire(fact);
			assertEquals(RulesExecutor.ExecutionResult.FIRED_RULES_MAX, result.getNumberOfFiredRules());
		}
	}

	/**
	 * @see https://issues.redhat.com/browse/DROOLS-7195
	 */
	@ParameterizedTest
	@EnumSource(TestScenario.class)
	void maxNumberOfRules_mvelSyntax(TestScenario scenario) {
		var fact = TestData.person();

		// Fails with executable model
		try (var executor = executor(scenario, "ModifyTest_maxNumberOfRules_mvel.drl")) {
			var result = executor.fire(fact);
			assertEquals(RulesExecutor.ExecutionResult.FIRED_RULES_MAX, result.getNumberOfFiredRules());
		}
	}

	/**
	 * @see https://issues.redhat.com/browse/DROOLS-5242
	 */
	@ParameterizedTest
	@EnumSource(TestScenario.class)
	void maxNumberOfRules_mvelQualifiedSyntax(TestScenario scenario) {
		// Fails with executable model
		try (var executor = executor(scenario, "ModifyTest_maxNumberOfRules_mvel_qualified.drl")) {
			fail("Should have failed");
		} catch (InvalidRulesException e) {
			// OK
		}
	}

}
