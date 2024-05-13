package drools.issues.model.vehicles;

import java.io.Serializable;

public class VehicleError implements Serializable {

	private final String message;
	private final Vehicle vehicle;

	public VehicleError(Vehicle vehicle,  String message) {
		this.message = message;
		this.vehicle = vehicle;
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}


	public String toString() {
		return "VehicleError:" + message;
	}
}
