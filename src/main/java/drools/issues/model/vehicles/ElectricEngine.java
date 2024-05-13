package drools.issues.model.vehicles;

public class ElectricEngine extends Engine {
	private final int batterySize;

	public ElectricEngine(int kw, int batterySize) {
		super(kw);
		this.batterySize = batterySize;
	}

	@Override
	public boolean isZeroEmissions() {
		return true;
	}

	public int getBatterySize() {
		return batterySize;
	}

}
