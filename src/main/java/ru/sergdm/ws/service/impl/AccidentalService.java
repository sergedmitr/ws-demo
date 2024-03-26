package ru.sergdm.ws.service.impl;

import org.springframework.stereotype.Service;
import ru.sergdm.ws.service.IAccidentalService;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccidentalService implements IAccidentalService {

	@Override
	public int accidental() throws InterruptedException{
		Random r = new Random();
		int mls = r.nextInt(1000 + 1);
		TimeUnit.MILLISECONDS.sleep(mls);
		return mls;
	}
}
