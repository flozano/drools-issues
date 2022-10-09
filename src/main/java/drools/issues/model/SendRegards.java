package drools.issues.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SendRegards {
	private String message;
	private final Map<String, String> params = new HashMap<>();
	private boolean send = true;

	public SendRegards(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSend() {
		return send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

	public SendRegards withParam(String name, String value) {
		params.put(name, Objects.requireNonNull(value));
		return this;
	}

	public String getParam(String name) {
		return params.get(name);
	}
}
