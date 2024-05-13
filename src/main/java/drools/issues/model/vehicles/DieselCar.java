package drools.issues.model.vehicles;

public class DieselCar extends Vehicle<DieselEngine> {
	private final DieselEngine engine;
	
	private long frameMaxTorque;
	
	

	public DieselCar(String maker, String model, int kw, boolean adBlueRequired) {
		super(maker, model);
		this.engine = new DieselEngine(kw, adBlueRequired);
	}

	@Override
	public DieselEngine getEngine() {
		return engine;
	}

	public long getFrameMaxTorque() {
		return frameMaxTorque;
	}

	public void setFrameMaxTorque(long frameMaxTorque) {
		this.frameMaxTorque = frameMaxTorque;
	}
}
