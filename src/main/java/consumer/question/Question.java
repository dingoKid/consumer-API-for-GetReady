package consumer.question;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Question implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4295725944679485422L;
	private String question;
	private String answer;
	private String info;
	
	@JsonIgnore
	private String labels;
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
		
	public String getLabels() {
		return labels;
	}
	public void setLabels(String labels) {
		this.labels = labels;
	}
	
	@Override
	public String toString() {
		return "Question [question=" + question + ", answer=" + answer + ", info=" + info + "]";
	}
	
}
