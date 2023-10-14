package ru.sergdm.ws.service;

import ru.sergdm.ws.model.Session;

public interface IStorageService {
	boolean existsSession(String session);
	void storeSession(Session session);
	void eraseSession(String session);

	Session getSession(String session);

	void storeUser(String login, String password);

	String getPassword(String login);
}
