package drools.issues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hpsf.Property;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import drools.issues.model.vehicles.GasolineModelGenerator;
import drools.issues.model.vehicles.Vehicle;
import drools.issues.model.vehicles.VehicleError;

public class DynamicClassTest extends AbstractTest {
	
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
			var result = executor.fire(vehicle1);
			assertEquals(3, result.getNumberOfFiredRules());
			assertEquals(2, result.getSingleOutput(Vehicle.class).getScore());
			
			var result2 = executor.fire(vehicle2);
			assertEquals(1, result2.getNumberOfFiredRules());
			assertEquals(0, result2.getSingleOutput(Vehicle.class).getScore());
			assertNotNull(result2.getSingleOutput(VehicleError.class));
			assertSame(vehicle2, result2.getSingleOutput(VehicleError.class).getVehicle());
		}
	}
	
}
