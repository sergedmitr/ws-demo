package ru.sergdm.ws.service;

public interface ILoginService {
	void createUser(String login, String password);
	boolean checkCredentials(String login, String password);
}
