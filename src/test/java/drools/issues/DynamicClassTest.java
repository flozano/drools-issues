package drools.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;


import org.apache.commons.beanutils.PropertyUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import drools.issues.executor.RulesExecutor;
import drools.issues.model.vehicles.DieselCar;
import drools.issues.model.vehicles.GasolineModelGenerator;
import drools.issues.model.vehicles.Vehicle;
import drools.issues.model.vehicles.VehicleError;

public class DynamicClassTest extends AbstractTest {
	static {
		System.setProperty("drools.dialect.java.compiler", "NATIVE");
		System.setProperty("drools.dialect.java.compiler.lnglevel", "17");
	}
	
	@ParameterizedTest
	@EnumSource(TestScenario.class)
	public void testStaticForComparison(TestScenario scenario) {
		DieselCar vehicle1 = new DieselCar("Volkswagen", "Passat", 100, true);
		vehicle1.setFrameMaxTorque(500);
		vehicle1.getEngine().setMaxTorque(350);
		vehicle1.getEngine().setSerialNumber(75_000);
		vehicle1.setScore(0);

		DieselCar vehicle2 = new DieselCar("Peugeot", "208", 50, true);
		vehicle2.setFrameMaxTorque(100);
		vehicle2.getEngine().setMaxTorque(200);
		vehicle2.setScore(0);

		try (var executor = executor(scenario, "DynamicClassTest_static.drl")) {
			RulesExecutor.ExecutionResult result = executor.fire(vehicle1);
			assertEquals(3, result.getNumberOfFiredRules());
			assertEquals(2, vehicle1.getScore());
			
			RulesExecutor.ExecutionResult result2 = executor.fire(vehicle2);
			assertEquals(1, result2.getNumberOfFiredRules());
			assertEquals(0, result2.getSingleOutput(Vehicle.class).getScore());
			assertNotNull(result2.getSingleOutput(VehicleError.class));
			assertSame(vehicle2, result2.getSingleOutput(VehicleError.class).getVehicle());
		}
	}

	@ParameterizedTest
	@EnumSource(TestScenario.class)
	public void test(TestScenario scenario)
			throws ReflectiveOperationException {
		GasolineModelGenerator generator = new GasolineModelGenerator(getClass().getClassLoader());
		
		
		var vehicle1 = generator.getVehicle("Toyota", "Supra", 150);
		// Newer unit
		PropertyUtils.setProperty(vehicle1, "engine.serialNumber", (long) 75_000);
		
		var vehicle2 = generator.getVehicle("Toyota", "Yaris", 75);
		// Weak frame
		PropertyUtils.setProperty(vehicle2, "frameMaxTorque", (long) 250);
		// Strong engine
		PropertyUtils.setProperty(vehicle2, "engine.maxTorque", 350);
		
		try (var executor = executor(scenario, "DynamicClassTest.drl", generator.getClassLoader())) {
			RulesExecutor.ExecutionResult result = executor.fire(vehicle1);
			assertEquals(3, result.getNumberOfFiredRules());
			assertEquals(2, result.getSingleOutput(Vehicle.class).getScore());
			
			RulesExecutor.ExecutionResult result2 = executor.fire(vehicle2);
			assertEquals(1, result2.getNumberOfFiredRules());
			assertEquals(0, result2.getSingleOutput(Vehicle.class).getScore());
			assertNotNull(result2.getSingleOutput(VehicleError.class));
			assertSame(vehicle2, result2.getSingleOutput(VehicleError.class).getVehicle());
		}
	}
	
}
