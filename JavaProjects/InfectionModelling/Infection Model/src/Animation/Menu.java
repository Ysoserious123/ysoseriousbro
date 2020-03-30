package Animation;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Menu {
	private JFrame frm;

	public Menu() {
		initialize();
	}

	public void initialize() {

		ButtonGroup group = new ButtonGroup();

		Font smallLabelFont = new Font("Tahoma", Font.BOLD, 20);
		Font descriptionFont = new Font("Tahoma", Font.BOLD, 15);

		frm = new JFrame("Infection Modeling");
		frm.setBounds(200, 200, 940, 580);
		frm.getContentPane().setBackground(Color.BLACK);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.getContentPane().setLayout(null);

		JLabel title = new JLabel("Infection Spread Simulation");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Tahoma", Font.BOLD, 30));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(190, 30, 500, 35);
		frm.add(title);

		JTextField numPersons = new JTextField("300");
		numPersons.setBounds(500, 100, 70, 20);
		frm.add(numPersons);

		JLabel people = new JLabel("Number of People:");
		people.setForeground(Color.WHITE);
		people.setFont(smallLabelFont);
		people.setBounds(300, 98, 300, 20);
		frm.add(people);
		
		JTextArea descrip1 = new JTextArea("My system seems to lag more around 2500 people\n                       so continue as you wish.");
		descrip1.setBackground(Color.BLACK);
		descrip1.setForeground(Color.GREEN);
		descrip1.setFont(descriptionFont);
		descrip1.setBounds(250, 138, 500, 40);
		frm.add(descrip1);

		JTextField percent = new JTextField("0");
		percent.setBounds(500, 190, 70, 20);
		frm.add(percent);

		JLabel distanced = new JLabel("Percent Distanced:");
		distanced.setForeground(Color.WHITE);
		distanced.setFont(smallLabelFont);
		distanced.setBounds(300, 188, 300, 20);
		frm.add(distanced);

		JTextField peopleSize = new JTextField("2");
		peopleSize.setBounds(500, 230, 70, 20);
		frm.add(peopleSize);

		JLabel radius = new JLabel("Person Size:");
		radius.setForeground(Color.WHITE);
		radius.setFont(smallLabelFont);
		radius.setBounds(300, 228, 300, 25);
		frm.add(radius);
		
		
		JTextArea descrip2 = new JTextArea("Collisions become strange when this is greater than about 8.");
		descrip2.setBackground(Color.BLACK);
		descrip2.setForeground(Color.MAGENTA);
		descrip2.setFont(descriptionFont);
		descrip2.setBounds(215, 268, 500, 15);
		frm.add(descrip2);

		JTextField speed = new JTextField("1");
		speed.setBounds(500, 300, 70, 20);
		frm.add(speed);

		JLabel personSpeed = new JLabel("Person Speed:");
		personSpeed.setForeground(Color.WHITE);
		personSpeed.setFont(smallLabelFont);
		personSpeed.setBounds(300, 298, 300, 25);
		frm.add(personSpeed);
		
		JTextArea descrip3 = new JTextArea("As speed increses the colision detection might not work properly");
		descrip3.setBackground(Color.BLACK);
		descrip3.setForeground(Color.RED);
		descrip3.setFont(descriptionFont);
		descrip3.setBounds(210, 338, 500, 40);
		frm.add(descrip3);


		JTextField width = new JTextField("700");
		width.setBounds(500, 380, 70, 20);
		frm.add(width);

		JLabel windowWidth = new JLabel("Window Width:");
		windowWidth.setForeground(Color.WHITE);
		windowWidth.setFont(smallLabelFont);
		windowWidth.setBounds(300, 378, 300, 20);
		frm.add(windowWidth);

		JTextField height = new JTextField("500");
		height.setBounds(500, 420, 70, 20);
		frm.add(height);

		JLabel windowHeight = new JLabel("Window Width:");
		windowHeight.setForeground(Color.WHITE);
		windowHeight.setFont(smallLabelFont);
		windowHeight.setBounds(300, 418, 300, 20);
		frm.add(windowHeight);

		JButton run = new JButton("Run");
		run.setBounds(380, 460, 100, 50);
		frm.add(run);
		frm.getRootPane().setDefaultButton(run);

		run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int numPeople = Integer.parseInt(numPersons.getText());
				int radius = Integer.parseInt(peopleSize.getText());
				int movementSpeed = Integer.parseInt(speed.getText());
				int windowWidth = Integer.parseInt(width.getText());
				int windowHeight = Integer.parseInt(height.getText());
				int percentDistanced = Integer.parseInt(percent.getText());
				Random rnd = new Random();
				AnimationWindow win = null;
				String name = "Infection Spread Simulation";

				try {
					
					frm.setState(JFrame.ICONIFIED);
					
					win = new AnimationWindow(numPeople, radius, windowWidth, windowHeight, rnd, movementSpeed,
							percentDistanced, "(" + numPeople + " people, " + radius + " person size, " + percentDistanced + " % social distancing)");
					

					JFrame frm1 = new JFrame(name);
					frm1.setBounds(0, 0, windowWidth, windowHeight);
					frm1.setFocusable(false);
					frm1.setResizable(false);
					frm1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frm1.add(win);
					frm1.setVisible(true);
					
				} catch (Exception r) {
					JOptionPane.showMessageDialog(null, "Invalid Inputs", "Run Error", JOptionPane.ERROR_MESSAGE);

				}
			}
		});

	}
	
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu menu = new Menu();
					menu.frm.setFocusable(false);
					menu.frm.setVisible(true);
					//Create Save Folder
					if(!InstantiateSave.doesFolderExist()) {
						InstantiateSave.CanAddFolder();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
