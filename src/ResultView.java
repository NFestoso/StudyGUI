import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Post quiz results
 * 
 * @author NathanFestoso
 */
public class ResultView extends JFrame {
	private static final long serialVersionUID = 1L;

	// Visual components
	private JPanel contentPane;
	private JLabel title;
	private JPanel panel;
	private JButton replay;
	private JButton ok;
	private JTable table;

	/**
	 * Create the frame.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public ResultView(Quiz quiz, int[] order, ArrayList<String> wrongAnswers) {
		// Center on screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 345;
		int height = 476;
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

		// Button pane
		panel = new JPanel();
		panel.setBounds(10, 394, 318, 31);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(0, 2, 5, 5));

		/*Quiz title*/
		title = new JLabel();
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(21, 10, 297, 26);
		title.setFont(new Font("Calibri", Font.PLAIN, 18));
		title.setText(quiz.getTitle());
		contentPane.add(title);

		/*Replay quiz*/
		replay = new JButton("Replay");
		panel.add(replay);

		/*Back to main menu*/
		ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		panel.add(ok);

		// Scroll pane for results
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 34, 318, 354);
		scrollPane.setBackground(Color.WHITE);
		contentPane.add(scrollPane);

		// Display results
		table = new JTable(new DefaultTableModel(new Object[] {"1", "2"}, 0));
		table.getColumnModel().getColumn(0).setMaxWidth(45);
		table.setBackground(new Color(255, 255, 255));
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int index;
		String question;
		String correctAnswer;
		String userAnswer;
		for(int i=0, k=0; i<quiz.getSize(); i++) {
			index = order[i];
			question = quiz.getQuestion(index);
			correctAnswer = quiz.getAnswer(index);
			model.addRow(new Object[] {"Q",question});
			if(quiz.getResult(i)) {
				//correct
				model.addRow(new Object[] {"", correctAnswer});
			}else {
				// incorrect
				userAnswer = wrongAnswers.get(k++);
				model.addRow(new Object[] {"X", userAnswer});
				model.addRow(new Object[] {"", correctAnswer});
			}
			model.addRow(new Object[] {"",""});
		}
		table.setShowVerticalLines(false);
		table.setShowGrid(false);
		table.setRowSelectionAllowed(false);
		table.setFont(new Font("Arial", Font.PLAIN, 21));
		scrollPane.setViewportView(table);
	}
}
