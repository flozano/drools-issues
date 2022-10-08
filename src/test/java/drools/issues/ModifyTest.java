package drools.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import drools.issues.executor.InvalidRulesException;
import drools.issues.executor.RulesExecutor;
import drools.issues.executor.RuntimeType;

class ModifyTest extends AbstractTest {

	@ParameterizedTest
	@EnumSource(RuntimeType.class)
	void maxNumberOfRules_getterSyntax(RuntimeType runtimeType) {
		var fact = TestData.person();

		// OK with both mvel and executable model
		try (var executor = executor(runtimeType, "ModifyTest_maxNumberOfRules_getter.drl")) {
			var result = executor.fire(fact);
			assertEquals(RulesExecutor.ExecutionResult.FIRED_RULES_MAX, result.getNumberOfFiredRules());
		}
	}

	@ParameterizedTest
	@EnumSource(RuntimeType.class)
	void maxNumberOfRules_mvelSyntax(RuntimeType runtimeType) {
		var fact = TestData.person();

		// Fails with executable model
		try (var executor = executor(runtimeType, "ModifyTest_maxNumberOfRules_mvel.drl")) {
			var result = executor.fire(fact);
			assertEquals(RulesExecutor.ExecutionResult.FIRED_RULES_MAX, result.getNumberOfFiredRules());
		}
	}

	@ParameterizedTest
	@EnumSource(RuntimeType.class)
	void maxNumberOfRules_mvelQualifiedSyntax(RuntimeType runtimeType) {
		// Fails with executable model
		try (var executor = executor(runtimeType, "ModifyTest_maxNumberOfRules_mvel_qualified.drl")) {
			fail("Should have failed");
		} catch (InvalidRulesException e) {
			// OK
		}
	}

}
