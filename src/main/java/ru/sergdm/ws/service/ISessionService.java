package ru.sergdm.ws.service;

public interface ISessionService {
	boolean isValidSession(String session);
	String createSession(String login);
	void dropSession(String session);

	String getUserFromSession(String session);
}
