package ru.sergdm.ws;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ApiController {

	@GetMapping("/health")
	public ResponseEntity<Object> health() {
		Health health = new Health("OK");
		return new ResponseEntity<>(health, HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<Object> name() {
		SystemName name = new SystemName();
		return new ResponseEntity<>(name, HttpStatus.OK);
	}
	
	@GetMapping("/name")
	public ResponseEntity<Object> fullName() {
		SystemName name = new SystemName();
		return new ResponseEntity<>(name, HttpStatus.OK);
	}
	
	@PostMapping(value = "/auth")
	public ResponseEntity<Object> auth(@RequestBody AuthRequest request) {
		AuthResponse response = new AuthResponse();
		response.valid = request.login.equals(request.password);
		HttpHeaders httpHeaders = new HttpHeaders();
		if (response.valid) {
			httpHeaders.add("x-username", request.login);
		}
		return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
	}
	
	@GetMapping(value = "/set-cookie")
	public ResponseEntity<?> setCookie(HttpServletResponse response) throws IOException {
		Cookie cookie = new Cookie("data", "Use_the-force_Luk");
		cookie.setPath("/");
		cookie.setMaxAge(86400);
		response.addCookie(cookie);
		response.setContentType("text/plain");
		return ResponseEntity.ok().body(HttpStatus.OK);
	}
	@GetMapping(value = "/get-cookie")
	public ResponseEntity<?> readCookie(@CookieValue(value = "data") String data) {
		return ResponseEntity.ok().body(data);
	}
}
