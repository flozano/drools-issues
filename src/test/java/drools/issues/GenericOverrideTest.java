package drools.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import drools.issues.executor.RuntimeType;
import drools.issues.model.vehicles.DieselCar;
import drools.issues.model.vehicles.ElectricCar;

/**
 * @see https://issues.redhat.com/browse/DROOLS-7197
 */
class GenericOverrideTest extends AbstractTest {

	@ParameterizedTest
	@EnumSource(TestScenario.class)
	public void ensureOverrideWorks(TestScenario scenario) {
		try (var executor = executor(scenario, "GenericOverrideTest_ensureOverrideWorks.drl")) {
			var golfTdi = new DieselCar("VW", "Golf 1.6 TDI", 85, true);
			var result = executor.fire(golfTdi);
			assertEquals(1, result.getNumberOfFiredRules());
			assertEquals(5, golfTdi.getScore());
		}
	}

	@ParameterizedTest
	@EnumSource(TestScenario.class)
	public void ensureOverrideWorksOK(TestScenario scenario) {
		try (var executor = executor(scenario, "GenericOverrideTest_ensureOverrideWorksOK.drl")) {
			var teslaModel3 = new ElectricCar("Tesla", "Model 3", 200, 90);
			var result = executor.fire(teslaModel3);
			assertEquals(1, result.getNumberOfFiredRules());
			assertEquals(10, teslaModel3.getScore());
		}
	}
}
