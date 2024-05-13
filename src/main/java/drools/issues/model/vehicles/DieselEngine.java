package drools.issues.model.vehicles;

public class DieselEngine extends Engine {

	private boolean dirty;

	// diesel has no octanes... but let's pretend it does
	private boolean highOctane;

	private int maxTorque;

	private long serialNumber;

	private final boolean adBlueRequired;

	public DieselEngine(int kw, boolean adBlueRequired) {
		super(kw);
		this.adBlueRequired = adBlueRequired;
	}

	public boolean isAdBlueRequired() {
		return adBlueRequired;
	}

	@Override
	public boolean isZeroEmissions() {
		return false;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean isHighOctane() {
		return highOctane;
	}

	public void setHighOctane(boolean highOctane) {
		this.highOctane = highOctane;
	}

	public int getMaxTorque() {
		return maxTorque;
	}

	public void setMaxTorque(int maxTorque) {
		this.maxTorque = maxTorque;
	}

	public void setSerialNumber(long serialNumber) {
		this.serialNumber = serialNumber;
	}

	public long getSerialNumber() {
		return serialNumber;
	}

}
