package ru.sergdm.ws.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.sergdm.ws.model.Session;
import ru.sergdm.ws.service.IStorageService;

@Service
public class StorageService implements IStorageService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Map<String, Session> sessions = new HashMap<>();
	private Map<String, String> credentials = new HashMap<>();
	
	@Override
	public void storeSession(Session session) {
		sessions.put(session.sessionId, session);
		logger.info("sessions_st = {}", sessions);
	}
	
	@Override
	public void eraseSession(String session) {
		sessions.remove(session);
		logger.info("sessions_er = {}", sessions);
	}
	
	@Override
	public boolean existsSession(String session) {
		logger.info("sessions_ex = {}", sessions);
		return sessions.containsKey(session);
	}
	@Override
	public Session getSession(String session) {
		logger.info("sessions_ex = {}", sessions);
		return sessions.get(session);
	};

	@Override
	public void storeUser(String login, String password) {
		credentials.put(login, password);
		logger.info("credentials_st = {}", credentials);
	};

	@Override
	public String getPassword(String login) {
		logger.info("login = {}", login);
		logger.info("credentials_get = {}", credentials);
		return credentials.get(login);
	};
}
