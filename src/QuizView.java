import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.JTextPane;
import java.awt.Toolkit;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.ActionEvent;

/**
 * Displays running quiz to user
 * 
 * @author NathanFestoso
 */
public class QuizView extends JFrame {
	private static final long serialVersionUID = 1L;

	// Components
	private ResultView results;
	private Quiz quiz;
	private JPanel contentPane;
	private JTextField answerBox;
	private JLabel quizName ;
	private JTextPane question;
	private JButton submit;
	private JPanel statusBar;

	// Functional components
	ArrayList<Integer> used;
	ArrayList<String> wrongAnswers;
	Random random;
	int randomNum;
	int currentQuestion;

	/*
	 * Create the frame
	 */
	public QuizView(Quiz quiz) {
		this.quiz = quiz;
		used = new ArrayList<>(quiz.getSize());
		wrongAnswers = new ArrayList<>();
		random = new Random();
		currentQuestion = 0;

		// Center on screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 394;
		int height = 290;
		int x = (screen.width-width)/2;
		int y = (screen.height-height)/2;
		setBounds(x, y, width, height);

		// Window
		setTitle("Study");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Quiz title
		quizName = new JLabel();
		quizName.setHorizontalAlignment(SwingConstants.CENTER);
		quizName.setFont(new Font("Arial", Font.PLAIN, 18));
		quizName.setBounds(8, 10, 372, 26);
		quizName.setText(quiz.getTitle());
		contentPane.add(quizName);

		// Question display
		question = new JTextPane();
		question.setOpaque(true);
		question.setEditable(false);
		question.setFont(new Font("Consolas", Font.PLAIN, 14));
		question.setBackground(Color.WHITE);
		question.setBounds(8, 48, 372, 89);
		// First random question
		randomNum = randomize(quiz.getSize());
		question.setText(quiz.getQuestion(randomNum));
		contentPane.add(question);

		// Answer field
		answerBox = new JTextField();
		// set default focus on text field
		super.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				answerBox.requestFocus();
			}
		});
		answerBox.setFocusable(true);
		answerBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				submit.doClick();
			}
		});
		answerBox.setFont(new Font("Arial", Font.PLAIN, 12));
		answerBox.setBounds(45, 158, 297, 21);
		contentPane.add(answerBox);
		answerBox.setColumns(100);

		// Lock in answer
		submit = new JButton("OK");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String answer = answerBox.getText().trim();

				if(!answer.equals("")) {
					if(quiz.submitAnswer(randomNum, answer)) {
						// Correct answer
						statusBar.getComponent(currentQuestion-1).setBackground(Color.GREEN);
					} else {
						// Incorrect
						statusBar.getComponent(currentQuestion-1).setBackground(Color.RED);
						wrongAnswers.add(answer);
					}

					randomNum = randomize(quiz.getSize());
					if(randomNum == -1 ) {
						QuizView.this.actionPerformed();
					}else{
						question.setText(quiz.getQuestion(randomNum));
						answerBox.setText("");
					}
				}
			}
		});
		submit.setFont(new Font("Arial", Font.PLAIN, 14));
		submit.setBounds(155, 196, 77, 26);
		contentPane.add(submit);

		// animated status bar
		statusBar = new JPanel(new GridLayout(1, quiz.getSize(), 3, 3));
		statusBar.setBounds(8, 45, 372, 5);
		contentPane.add(statusBar);
		// set to default status
		JLabel l;
		for(int i=0; i<quiz.getSize(); i++) {
			l = new JLabel();
			l.setOpaque(true);
			statusBar.add(l);
		}


	}

	/*
	 *  Generates random numbers for question order
	 *  @returns -1 upon completion
	 */
	int randomize(int range){
		boolean done = false;

		while(!done){
			for(int i=0; i<range; i++){
				randomNum = random.nextInt(range);
				if(!used.contains(randomNum)){
					used.add(randomNum);
					currentQuestion++;
					return randomNum;
				}else if(used.size() >= range){
					done = true;
				}
			}
		}
		return -1;
	}


	public void actionPerformed() {
		int[] questionOrder = new int[quiz.getSize()];

		for(int i=0; i<used.size(); i++)
			questionOrder[i] = used.get(i);

		results = new ResultView(quiz, questionOrder, wrongAnswers);
		results.setVisible(true);
		this.dispose();
	}


}
