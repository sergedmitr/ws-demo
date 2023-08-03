package ru.sergdm.ws;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

	@GetMapping("/health")
	public ResponseEntity<Object> health() {
		Health health = new Health("OK");
		return new ResponseEntity<>(health, HttpStatus.OK);
	}
	
	@GetMapping("/name")
	public ResponseEntity<Object> name() {
		SystemName name = new SystemName();
		return new ResponseEntity<>(name, HttpStatus.OK);
	}
	
}
