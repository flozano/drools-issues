package drools.issues.model.vehicles;

public abstract class Engine {

	private final int kw;

	public Engine(int kw) {
		this.kw = kw;
	}

	public int getKw() {
		return kw;
	}

	public abstract boolean isZeroEmissions();

}
