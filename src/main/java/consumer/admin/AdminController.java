package consumer.admin;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import consumer.model.QuestionDto;
import consumer.model.User;
import consumer.question.Question;

@Controller
public class AdminController {

	private static String LOGIN;
	private static String GET_LABELS;
	private static String DELETE_QUESTION;
	private static String DELETE_LABEL;
	private static String BEARER_TOKEN;
	private static String ADD_QUESTION;
	private static String ADD_LABEL;

	@Autowired
	RestTemplate restTemplate;

	@Value("${paths.getLabels}")
	public void setGetLabels(String getLabels) {
		GET_LABELS = getLabels;
	}

	@Value("${paths.deleteQuestion}")
	public void setDeleteQuestion(String deleteQuestions) {
		DELETE_QUESTION = deleteQuestions;
	}

	@Value("${paths.login}")
	public void setLogin(String login) {
		LOGIN = login;
	}

	@Value("${paths.addQuestion}")
	public void setAddQuestion(String addQuestions) {
		ADD_QUESTION = addQuestions;
	}

	@Value("${paths.addLabel}")
	public void setAddLabel(String addLabel) {
		ADD_LABEL = addLabel;
	}
	
	@Value("${paths.deleteLabel}")
	public void setDeleteLabel(String deleteLabel) {
		DELETE_LABEL = deleteLabel;
	}

	@GetMapping("/login")
	public String login(Map<String, Object> model) {
		model.put("user", new User());
		return "login";
	}

	@GetMapping("/admin")
	public String home(Map<String, Object> model) {
		String[] labels = restTemplate.getForEntity(GET_LABELS, String[].class).getBody();
		model.put("allLabels", labels);
		model.put("question", new Question());
		return "admin";
	}

	@PostMapping("/admin")
	public String login(User user) {
		HttpEntity<User> authEntity = new HttpEntity<User>(user);
		try {
			ResponseEntity<String> result = restTemplate.exchange(LOGIN, HttpMethod.POST, authEntity, String.class);
			BEARER_TOKEN = "Bearer " + result.getBody();
			return "redirect:/admin";
		} catch (HttpClientErrorException e) {
			return "login";
		}
	}

	@GetMapping("/api/delete")
	public String deleteQuestion(String questionToDelete, Map<String, Object> model) {
		if (!questionToDelete.isEmpty()) {
			try {
				restTemplate.exchange(DELETE_QUESTION + questionToDelete, HttpMethod.DELETE,
						new HttpEntity<String>(getHeader()), Void.class);
			} catch (HttpClientErrorException | HttpServerErrorException e) {
				model.put("user", new User());
				return "login";
			}
		}
		return "redirect:/admin";
	}

	@PostMapping("/api/addquestion")
	public String addQuestion(Question question, Map<String, Object> model) {
		if (question.getQuestion().isEmpty() || question.getAnswer().isEmpty() || question.getLabels() == null) {
			return "redirect:/admin";
		}
		QuestionDto dto = new QuestionDto();
		dto.setQuestion(question.getQuestion());
		dto.setAnswer(question.getAnswer());
		dto.setInfo(question.getInfo());
		dto.setLabels(Arrays.stream(question.getLabels().split(",")).collect(Collectors.toSet()));

		try {
			restTemplate.exchange(ADD_QUESTION, HttpMethod.POST, new HttpEntity<QuestionDto>(dto, getHeader()),
					Void.class);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			model.put("user", new User());
			return "login";
		}
		return "redirect:/";
	}

	@GetMapping("/api/addlabel")
	public String addLabel(String label, Map<String, Object> model) {
		if (!label.isEmpty()) {
			try {
				restTemplate.exchange(ADD_LABEL + label, HttpMethod.GET, new HttpEntity<String>(getHeader()), Void.class);
			} catch (HttpClientErrorException | HttpServerErrorException e) {
				model.put("user", new User());
				return "login";
			}
		}
		return "redirect:/admin";
	}
	
	@GetMapping("/api/deletelabel")
	public String deleteLabel(String label, Map<String, Object> model) {
		if(!label.isEmpty()) {
			try {
				restTemplate.exchange(DELETE_LABEL + label, HttpMethod.DELETE, new HttpEntity<String>(getHeader()), Void.class);
			} catch (HttpClientErrorException | HttpServerErrorException e) {
				model.put("user", new User());
				return "login";
			}
		}
		return "redirect:/admin";
	}

	private HttpHeaders getHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", BEARER_TOKEN);
		return headers;
	}

}
