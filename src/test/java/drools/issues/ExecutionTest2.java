package drools.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import drools.issues.executor.RuntimeType;
import drools.issues.model.Person;
import drools.issues.model.RegardsManager;
import drools.issues.model.SendRegards;

/**
 * @see https://issues.redhat.com/browse/DROOLS-7195
 */
class ExecutionTest2 extends AbstractTest {


	@ParameterizedTest
	@EnumSource(RuntimeType.class)
	public void helloWithMVELSyntax(RuntimeType runtimeType) {
		try (var executor = executor(runtimeType, "ExecutionTest2_hello.drl")) {
			executor.addGlobalVariable("regardsManager", new RegardsManager());
			var fact = donald();
			var result = executor.fire(fact);
			assertEquals(2, result.getNumberOfFiredRules());
			assertEquals(2, result.getOutput().size());
			var sendRegards = result.getSingleOutput(SendRegards.class);
			assertNotNull(sendRegards);
			assertEquals("Hi Donald", sendRegards.getMessage());
			assertFalse(sendRegards.isSend());
		}
	}

	@ParameterizedTest
	@EnumSource(RuntimeType.class)
	public void helloWithSetter(RuntimeType runtimeType) {
		try (var executor = executor(runtimeType, "ExecutionTest2_hello_setter.drl")) {
			executor.addGlobalVariable("regardsManager", new RegardsManager());
			var fact = donald();
			var result = executor.fire(fact);
			assertEquals(2, result.getNumberOfFiredRules());
			assertEquals(2, result.getOutput().size());
			var sendRegards = result.getSingleOutput(SendRegards.class);
			assertNotNull(sendRegards);
			assertEquals("Hi Donald", sendRegards.getMessage());
			assertFalse(sendRegards.isSend());
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
