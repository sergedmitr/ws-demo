package ru.sergdm.ws.model;

public class Session {
	public String sessionId;
	public String userName;

	@Override
	public String toString() {
		return "Session{" +
				"sessionId='" + sessionId + '\'' +
				", userName='" + userName + '\'' +
				'}';
	}
}
