package ru.sergdm.ws.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import ru.sergdm.ws.service.ILoginService;
import ru.sergdm.ws.service.IStorageService;

@Service
public class LoginService implements ILoginService{
	@Autowired
	IStorageService storageService;
	
	@Override
	public void createUser(String login, String password) {
		storageService.storeUser(login, password);
	}
	
	@Override
	public boolean checkCredentials(String login, String password) {
		String pwd = storageService.getPassword(login);
		return StringUtils.equals(pwd, password);
	}
}
