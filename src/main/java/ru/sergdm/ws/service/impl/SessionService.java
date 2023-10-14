package ru.sergdm.ws.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.uuid.Generators;

import ru.sergdm.ws.model.Session;
import ru.sergdm.ws.service.ISessionService;
import ru.sergdm.ws.service.IStorageService;

@Service
public class SessionService implements ISessionService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private IStorageService storage;
	
	@Override
	public boolean isValidSession(String session) {
		return storage.existsSession(session);
	}
	
	@Override
	public String createSession(String login) {
		logger.info("start create");
		Session session = new Session();
		session.sessionId = Generators.randomBasedGenerator().generate().toString();
		session.userName = login;
		storage.storeSession(session);
		return session.sessionId;
	}
	
	@Override
	public void dropSession(String session) {
		storage.eraseSession(session);
	}

	@Override
	public String getUserFromSession(String sessionId) {
		Session session = storage.getSession(sessionId);
		return session != null ? session.userName : null;
	}
}
