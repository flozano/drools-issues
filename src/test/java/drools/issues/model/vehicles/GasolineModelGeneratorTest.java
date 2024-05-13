package drools.issues.model.vehicles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class GasolineModelGeneratorTest {
	
	
	@Test
	public void test()
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		GasolineModelGenerator generator = new GasolineModelGenerator(getClass().getClassLoader());
		Class<? extends Vehicle> vehicleClass = generator.getVehicleClass();
		Class<? extends Engine> engineClass = generator.getEngineClass();
		var engine = engineClass.getConstructor(int.class).newInstance(75);
		var constructor = vehicleClass.getConstructor(String.class, String.class, engineClass);
		Vehicle<?> gasolineVehicle = constructor.newInstance("Peugeot", "208", engine);
		assertEquals(75, gasolineVehicle.getEngine().getKw());
		assertFalse(gasolineVehicle.getEngine().isZeroEmissions());
		assertEquals("Peugeot", gasolineVehicle.getMaker());
		assertEquals("208", gasolineVehicle.getModel());
	}
}
