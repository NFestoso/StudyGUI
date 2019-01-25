/**
 * Quiz object
 * 
 * @author NathanFestoso
 */
public class Quiz {

	private String title;
	private String[] questions;
	private String[] answers;
	private boolean[] results;

	public Quiz(String title, String[] questions, String[] answers) {
		this.title = title;
		this.questions = questions;
		this.answers = answers;
		results = new boolean[getSize()];
	}

	public String getTitle() {
		return title;
	}

	public String getQuestion(int index) {
		return questions[index];
	}

	public String getAnswer(int index) {
		return answers[index];
	}

	public int getSize() {
		return questions.length;
	}

	public boolean getResult(int index) {
		return results[index];
	}

	public boolean submitAnswer(int question, String answer) {
		if(answers[question].trim().equalsIgnoreCase(answer)) {
			results[question] = true;
			return true;
		}
		results[question] = false;
		return false;
	}

}
