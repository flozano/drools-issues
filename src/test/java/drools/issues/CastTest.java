package drools.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import drools.issues.executor.RuntimeType;
import drools.issues.model.RegardsManager;
import drools.issues.model.SendRegards;
import drools.issues.model.vehicles.ElectricCar;

/**
 * @see https://issues.redhat.com/browse/DROOLS-7198
 */
class CastTest extends AbstractTest {

	@ParameterizedTest
	@EnumSource(TestScenario.class)
	public void intToString(TestScenario scenario) {
		try (var executor = executor(scenario, "CastTest_intToString.drl")) {
			executor.addGlobalVariable("regardsManager", new RegardsManager());
			var teslaModel3 = new ElectricCar("Tesla", "Model 3", 200, 90);
			teslaModel3.setScore(10);
			var result = executor.fire(teslaModel3);
			assertEquals(1, result.getNumberOfFiredRules());
			var regards = result.getSingleOutput(SendRegards.class);
			assertNotNull(regards);
			assertEquals("10", regards.getParam("score"));
		}
	}
}
