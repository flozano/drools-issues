package drools.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import drools.issues.executor.RulesExecutor;
import drools.issues.executor.RuntimeType;

class ModifyTest extends AbstractTest {

	@ParameterizedTest
	@EnumSource(RuntimeType.class)
	void maxNumberOfRules(RuntimeType runtimeType) {
		var executor = executor(runtimeType, "ModifyTest_maxNumberOfRules.drl");
		var fact = TestData.person();
		var result = executor.fire(fact);
		assertEquals(RulesExecutor.ExecutionResult.FIRED_RULES_MAX, result.getNumberOfFiredRules());
	}

}
