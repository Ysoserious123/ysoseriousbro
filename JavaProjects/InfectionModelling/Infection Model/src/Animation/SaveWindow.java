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

/*
 * This is the SaveWindow class. Its job is to create a GUI that enables the user to save the data from an animation they ran. Note that this class extends JDialog.
 */
public class SaveWindow extends JDialog implements ActionListener {

	// This is the class that will attempt to save the current graph
	private InstantiateSave save;
	// This is the default name of the graph that is being saved
	private String defaultTitle;
	// This is a text field that will allow the user to change the name of the file
	JTextField title;

	/*
	 * This is the constructor for SaveWindow. Its job is to initialize global variables  that were not initialized already and make this class visible.
	 * @param title = title of the graph that is being saved
	 * @param accessBy = the JFrame that this class will be created relative to
	 * @param data = Data class obtained from the animation that was just run
	 */
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

	/*
	 * This method initializes and adds all necessary components to this class.
	 */
	public void initialize() {

		// ArrayList to hold the components to be added
		ArrayList<JComponent> components = new ArrayList<JComponent>();

		// creates a text field that allows the user to name the file. The default file is the name of the graph or a version with that same name.
		title = new JTextField();
		title.setBounds(70, 20, 120, 20);
		title.setText(defaultTitle);
		// The next two lines highlight the text in the text field to allow for ease of editing
		title.setSelectionStart(0);
		title.setSelectionEnd(defaultTitle.length());
		components.add(title);

		// creates a label that titles the window
		JLabel lbl = new JLabel("Save File?");
		lbl.setBounds(65, 0, 150, 20);
		components.add(lbl);

		// creates a label next to the text field
		JLabel name = new JLabel("File Name:");
		name.setBounds(5, 20, 60, 20);
		components.add(name);

		// creates a JButton that will save the file and adds this class as an ActionListener. See actionPerformed() for more detail
		JButton save = new JButton("Save");
		save.setBounds(55, 45, 80, 20);
		save.addActionListener(this);
		components.add(save);

		// add all of the components
		for (int i = 0; i < components.size(); i++) {
			this.add(components.get(i));
		}
		
		//This allows the user to press enter to press the button
		this.getRootPane().setDefaultButton(save);

	}

	/*
	 * This overrides the method in the ActionListener class and calls on the InstantiateSave class to save the file when the save button is pressed
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//System.out.println(title.getText());
		
		// if the file can be saved then this window will disappear
		if (save.save(title.getText())) {
			this.dispose();
		}
	}
	/*
	 * This is the main method I used to test this class.
	 */

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
