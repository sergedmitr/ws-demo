package ru.sergdm.ws;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.uuid.Generators;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import ru.sergdm.ws.model.AuthRequest;
import ru.sergdm.ws.model.AuthResponse;
import ru.sergdm.ws.model.Credentials;
import ru.sergdm.ws.model.Health;
import ru.sergdm.ws.model.SystemName;
import ru.sergdm.ws.service.ILoginService;
import ru.sergdm.ws.service.ISessionService;

@RestController
public class ApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ISessionService sessionService;
	@Autowired
	private ILoginService loginService;

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

	@RequestMapping(value = "/auth/**")
	public ResponseEntity<?> authOnGet(@CookieValue(value = "session", required = false) String session) {
		logger.info("auth = {}", session);
		if (session == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		AuthResponse response = new AuthResponse();
		HttpHeaders httpHeaders = new HttpHeaders();
		Optional<String> user = Optional.ofNullable(sessionService.getUserFromSession(session));
		user.ifPresent(
				u -> { httpHeaders.add("x-username", u);
					response.valid = true;});
		if (user.isPresent()) {
			return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(response, httpHeaders, HttpStatus.UNAUTHORIZED);
		}
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
	
	@GetMapping(value = "/get-uuid")
	public ResponseEntity<?> generateUuid() {
		UUID uuid = Generators.randomBasedGenerator().generate();
		return ResponseEntity.ok().body(uuid);
	}
	
	@GetMapping(value = "/check-session")
	public ResponseEntity<?> checkSession(@RequestParam String session) {
		return ResponseEntity.ok().body(sessionService.isValidSession(session));
	}
	
	@PostMapping(value = "/create-session")
	public ResponseEntity<?> createSession() {
		return ResponseEntity.ok().body(sessionService.createSession("no_user"));
	}
	
	@DeleteMapping(value = "/drop-session")
	public ResponseEntity<?> dropSession(@RequestParam String session) {
		logger.info("session = {}", session);
		sessionService.dropSession(session);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/create-user")
	public ResponseEntity<?> createUser(@RequestBody Credentials credentials) {
		logger.info("create-user = {}", credentials);
		loginService.createUser(credentials.login, credentials.password);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/check-password")
	public ResponseEntity<?> checkPassword(@RequestBody Credentials credentials) {
		AuthResponse response = new AuthResponse();
		response.valid = loginService.checkCredentials(credentials.login, credentials.password);
		HttpHeaders httpHeaders = new HttpHeaders();
		if (response.valid) {
			httpHeaders.add("x-username", credentials.login);
		}
		return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
	}

	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody Credentials credentials, HttpServletResponse response) {
		if (loginService.checkCredentials(credentials.login, credentials.password)) {
			String session = sessionService.createSession(credentials.login);
			Cookie cookie = new Cookie("session", session);
			cookie.setPath("/");
			cookie.setMaxAge(900);
			response.addCookie(cookie);
			response.setContentType("text/plain");
			return ResponseEntity.ok().body(HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping(value = "/logout")
	public ResponseEntity<?> logout(@CookieValue(value = "session") String session) {
		if (session != null) {
			sessionService.dropSession(session);
		}
		return ResponseEntity.ok().body(HttpStatus.OK);
	}
}
