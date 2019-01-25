import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Main view used to choose and load quiz.
 * 
 * @author NathanFestoso
 */
public class MainView extends JFrame {

	// View components
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList<String> quizList;
	private JTextPane quizPreview;
	private JButton start;
	private JCheckBox showAnswerToggle;
	private JLabel showQuizSize;

	// Functional components
	private Scanner input;
	private FileReader readFile;
	private File[] quizFiles;
	private String fontfamily;
	private ArrayList<Quiz> quizzes = new ArrayList<Quiz>(20);
	private File quizFolder = new File("Quizzes");

	// Other Windows
	private QuizView runQuiz;


	/**
	 * Launch application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * Create main screen
	 * 
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public MainView() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// Open in middle of screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 690;
		int height = 510;
		int x = (screen.width-width)/2;
		int y = (screen.height-height)/2;
		setBounds(x, y, width, height);
		
		// Window
		setResizable(false);
		setTitle("Study");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// LOAD
		loadQuizzes();

		/* Displays list of quizzes loaded from quiz folder */
		quizList = new JList<String>(getQuizNames());
		quizList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2)
					start.doClick();
			}
		});
		quizList.setFont(new Font("Arial", Font.PLAIN, 15));
		quizList.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		quizList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		quizList.setBounds(10, 10, 215, 400);
		
		// update quiz preview for selected file
		quizList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				displayQuiz(quizList.getSelectedIndex());
			}
		});
		contentPane.add(quizList);

		/* Displays the questions and optional answers */
		quizPreview = new JTextPane();
		quizPreview.setEditable(false);
		quizPreview.setContentType("text/html");
		fontfamily = quizPreview.getFont().getFamily();
		quizPreview.setFont(new Font("Arial", Font.PLAIN, 16));
		quizPreview.setBounds(235, 10, 440, 403);
		contentPane.add(quizPreview);

		/* Start button - Run selected quiz */
		start = new JButton("START");
		start.setFont(new Font("Arial", Font.PLAIN, 21));
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(quizList.getSelectedIndex() >= 0) {
					if(quizzes.get(quizList.getSelectedIndex()).getSize() > 0) {
						// create quiz window and pass in quiz
						runQuiz = new QuizView(quizzes.get(quizList.getSelectedIndex()));
						runQuiz.setVisible(true);
					}
				}
			}
		});
		start.setBounds(10, 419, 216, 42);
		contentPane.add(start);


		/* Enables answers to be visible */
		showAnswerToggle = new JCheckBox("Show answers");
		showAnswerToggle.setFont(new Font("Arial", Font.PLAIN, 19));
		showAnswerToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayQuiz(quizList.getSelectedIndex());
			}
		});
		showAnswerToggle.setBounds(235, 423, 179, 35);
		contentPane.add(showAnswerToggle);


		/* Displays the number of questions in said quiz */
		showQuizSize = new JLabel();
		showQuizSize.setFont(new Font("Calibri Light", Font.PLAIN, 19));
		showQuizSize.setHorizontalAlignment(SwingConstants.RIGHT);
		showQuizSize.setBounds(431, 427, 244, 26);
		contentPane.add(showQuizSize);

	}

	/**
	 * Preview of quiz before starting
	 * 
	 * @param quizIndex quiz chosen by user
	 */
	private void displayQuiz(int quizIndex) {
		String quizDisplay;

		if(quizList.getSelectedIndex() >= 0) {
			// preview quiz with HTML format
			quizDisplay = "<html><body style=\"font-family: "+fontfamily+"\">";
			for(int i=0; i<quizzes.get(quizIndex).getSize(); i++) {
				quizDisplay += ("<b>Q: "+quizzes.get(quizIndex).getQuestion(i)+"</b><br/>");

				if(showAnswerToggle.isSelected())
					quizDisplay += ("A: "+quizzes.get(quizIndex).getAnswer(i)+"<br/><br/>");
				else
					quizDisplay += "<br/>";
			}
			quizDisplay += "</body></html>";
			quizPreview.setText(quizDisplay);

			if(quizzes.get(quizIndex).getSize() == 0)
				showQuizSize.setText("empty");
			else if(quizzes.get(quizIndex).getSize() == 1)
				showQuizSize.setText("1 question");
			else
				showQuizSize.setText(quizzes.get(quizIndex).getSize()+" questions");
		}

	}

	/**
	 * Get list of quiz names in 'Quizzes' folder
	 * 
	 * @return array of quiz names
	 */
	private String[] getQuizNames() {
		String[] names = new String[quizzes.size()];

		for(int i=0; i<names.length; i++) 
			names[i] = quizzes.get(i).getTitle();

		return names;
	}

	// Loads and creates quizzes from quiz folder
	private void loadQuizzes() throws IOException {
		String title;
		ArrayList<String> questions;
		ArrayList<String> answers;
		String[] fileData = new String[2];

		quizFiles = quizFolder.listFiles();

		// load each quiz
		for(int i=0; i<quizFiles.length; i++) {
			readFile = new FileReader(quizFiles[i]);
			input = new Scanner(readFile);

			questions = new ArrayList<>(20);
			answers = new ArrayList<>(20);

			// load quiz
			while(input.hasNextLine()) {
				fileData = input.nextLine().split("\t");
				questions.add(fileData[0].trim());
				answers.add(fileData[1].trim());
			}
			// remove file extension and assign title
			title = quizFiles[i].getName().substring(0, quizFiles[i].getName().indexOf("."));
			// create quiz
			quizzes.add(new Quiz(title, questions.toArray(new String[questions.size()]), answers.toArray(new String[answers.size()])));
		}
		input.close();
		readFile.close();
	}
}
