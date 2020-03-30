package Animation;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SaveWindow extends JDialog implements ActionListener {

	private InstantiateSave save;
	private String defaultTitle;
	JTextField title;

	public SaveWindow(String title, JFrame accessBy, Data data) {
		int width = 200;
		int height = 100;

		defaultTitle = title;

		save = new InstantiateSave(data, defaultTitle, this);

		defaultTitle = save.getFileName();

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setSize(width, height);
		this.setLocationRelativeTo(accessBy);
		this.setName("Save File");
		this.setResizable(false);
		this.setAutoRequestFocus(true);
		initialize();
	}

	public void initialize() {

		ArrayList<JComponent> components = new ArrayList<JComponent>();

		title = new JTextField();
		title.setBounds(70, 20, 120, 20);
		title.setText(defaultTitle);
		title.setSelectionStart(0);
		title.setSelectionEnd(defaultTitle.length());
		components.add(title);

		JLabel lbl = new JLabel("Save File?");
		lbl.setBounds(65, 0, 150, 20);
		components.add(lbl);

		JLabel name = new JLabel("File Name:");
		name.setBounds(5, 20, 60, 20);
		components.add(name);

		JButton save = new JButton("Save");
		save.setBounds(55, 45, 80, 20);
		save.addActionListener(this);
		components.add(save);

		for (int i = 0; i < components.size(); i++) {
			this.add(components.get(i));
		}
		
		this.getRootPane().setDefaultButton(save);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//System.out.println(title.getText());
		if (save.save(title.getText())) {
			this.dispose();
		}
	}

	/*
	 * public static void main(String[] args) { 
	 * 
	 * JFrame test = new JFrame("Test");
	 * test.setBounds(200, 200, 800, 200); test.setVisible(true);
	 * test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	 * SaveWindow win = new SaveWindow("hi", test);
	 * 
	 * }
	 */

}
