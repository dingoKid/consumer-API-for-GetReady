package consumer.question;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class QuestionController {

	private static String GET_LABELS;
	private static String GET_QUESTION;
	private static String SEARCH_QUESTIONS;

	@Autowired
	RestTemplate restTemplate;

	@Value("${paths.getLabels}")
	public void setGetLabels(String getLabels) {
		QuestionController.GET_LABELS = getLabels;
	}

	@Value("${paths.getQuestion}")
	public void setGetQuestion(String getQuestion) {
		QuestionController.GET_QUESTION = getQuestion;
	}

	@Value("${paths.searchQuestions}")
	public void setSearchQuestions(String searchQuestions) {
		QuestionController.SEARCH_QUESTIONS = searchQuestions;
	}

	@GetMapping("/")
	public String home(Map<String, Object> model) {
		String[] labels = restTemplate.getForEntity(GET_LABELS, String[].class).getBody();
		model.put("allLabels", labels);
		return "index";
	}

	@GetMapping("/randomquestion")
	public String getRandomQuestion(Map<String, Object> model) {
		Question randomQuestion = restTemplate.getForObject(GET_QUESTION, Question.class);
		model.put("question", randomQuestion);
		return "randomquestion";
	}

	@GetMapping("/search")
	public String searchByKeyword(String keyword, Map<String, Object> model) {
		if (keyword.isBlank())
			return "redirect:";
		Question[] questions = restTemplate.getForEntity(SEARCH_QUESTIONS + "/" + keyword, Question[].class).getBody();
		model.put("questions", questions);
		return "filteredquestions";
	}

	@PostMapping("/question")
	public String searchByQuestion(Question question, Map<String, Object> model) {
		model.put("question", question);
		return "randomquestion";
	}

	@GetMapping("/searchByLabels")
	public String searchByLabels(String labels, Map<String, Object> model) {
		if (labels == null)
			return "redirect:";
		List<String> labelsList = Arrays.asList(labels.split(","));
		ResponseEntity<Question> questionEntity = restTemplate.postForEntity(SEARCH_QUESTIONS, labelsList,
				Question.class);
		model.put("question", questionEntity.getBody());
		model.put("labels", labels);
		return "labeledquestion";
	}
}
