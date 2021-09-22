package consumer.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import consumer.model.User;

@Controller
public class AdminController {
	
	private static final String AUTH_PATH = "http://localhost:8081/api/login";
	private static String DELETE_QUESTION;
	private static String BEARER_TOKEN;

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${paths.deleteQuestion}")
	public void setDeleteQuestion(String deleteQuestions) {
		AdminController.DELETE_QUESTION = deleteQuestions;
	}
	
	@GetMapping("/login")
	public String login(Map<String, Object> model) {
		model.put("user", new User());
		return "login";
	}
	
	@GetMapping("/admin")
	public String home() {
		return "admin";
	}
	
	@PostMapping("/admin")
	public String login(User user) {
		HttpEntity<User> authEntity = new HttpEntity<User>(user);
		try {
			ResponseEntity<String> result = restTemplate.exchange(AUTH_PATH, HttpMethod.POST, authEntity, String.class);
			BEARER_TOKEN = "Bearer " + result.getBody();
			return "admin";
		} catch (HttpClientErrorException e) {
			return "login";
		}
	}
	
	@GetMapping("/api/delete")
	public String deleteQuestion(String questionToDelete, Map<String, Object> model) {
		if(!questionToDelete.isEmpty()) {
			DELETE_QUESTION += questionToDelete;
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", BEARER_TOKEN);
			try {
				restTemplate.exchange(DELETE_QUESTION, HttpMethod.DELETE, new HttpEntity<String>(headers), Void.class);
			} catch (HttpClientErrorException e) {
				System.out.println(e.getMessage());
				model.put("user", new User());
				return "login";
			}
		}
		return "redirect:/admin";
	}
	
}
