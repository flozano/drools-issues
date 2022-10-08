package drools.issues.model;

public class RegardsManager {
	public SendRegards requestSendRegards(String message) {
		return new SendRegards(message);
	}
}
