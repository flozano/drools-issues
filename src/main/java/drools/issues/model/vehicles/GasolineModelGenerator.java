package drools.issues.model.vehicles;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.matcher.ElementMatchers;

public class GasolineModelGenerator {

	private ClassLoader classLoader;
	private Class<? extends Engine> engineClass;
	private Class<? extends Vehicle> vehicleClass;

	public GasolineModelGenerator(ClassLoader initialClassLoader) {
		this.classLoader = initialClassLoader;
		try {
			engineClass = (Class<? extends Engine>) classLoader.loadClass(
					"drools.issues.model.vehicles.GasolineEngine");
			this.classLoader = engineClass.getClassLoader();
		} catch (ClassNotFoundException e) {
			engineClass = new ByteBuddy()
					.subclass(Engine.class).name("drools.issues.model.vehicles.GasolineEngine") //
					.method(ElementMatchers.named("isZeroEmissions")) //
					.intercept(FixedValue.value(false)) //
					.defineProperty("highOctane", boolean.class) //
					.defineProperty("maxTorque", int.class) //
					.defineProperty("serialNumber", long.class) //
					.make().load(classLoader, ClassLoadingStrategy.Default.CHILD_FIRST_PERSISTENT).getLoaded();
			this.classLoader = engineClass.getClassLoader();
		}

		try {
			vehicleClass = (Class<? extends Vehicle>) classLoader.loadClass(
					"drools.issues.model.vehicles.GasolineVehicle");
			this.classLoader = vehicleClass.getClassLoader();
		} catch (ClassNotFoundException e) {
			try {
				TypeDescription.Generic generic = TypeDescription.Generic.Builder.parameterizedType(Vehicle.class, engineClass).build();
				vehicleClass = (Class<? extends Vehicle>) new ByteBuddy() //
						// .subclass(Vehicle.class) //
						.subclass(generic)
						.name("drools.issues.model.vehicles.GasolineVehicle") //
						.defineField("engine", engineClass, Visibility.PRIVATE) //
						.defineConstructor(Visibility.PUBLIC) //
						.withParameters(String.class, String.class, engineClass) //
						.intercept(MethodCall.invoke(Vehicle.class.getDeclaredConstructor(String.class, String.class))
								.withArgument(0, 1) //
								.andThen(FieldAccessor.ofField("engine").setsArgumentAt(2)) //
						) //
						// .defineMethod("getEngine", engineClass, Visibility.PUBLIC) //
						.method(ElementMatchers.named("getEngine")) //
						.intercept(FieldAccessor.ofField("engine")) //
						.defineProperty("frameMaxTorque", long.class) //
						.make().load(classLoader, ClassLoadingStrategy.Default.CHILD_FIRST_PERSISTENT).getLoaded();
				this.classLoader = vehicleClass.getClassLoader();
			} catch (NoSuchMethodException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public Class<? extends Vehicle> getVehicleClass() {
		return vehicleClass;
	}

	public Class<? extends Engine> getEngineClass() {
		return engineClass;
	}

	public Vehicle getVehicle(String maker, String model, int kw) {
		try {
			var vehicle = vehicleClass.getConstructor(String.class, String.class, engineClass).newInstance(maker, model,
					engineClass.getConstructor(int.class).newInstance(kw));
			vehicle.setScore(0);
			return vehicle;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}
}
