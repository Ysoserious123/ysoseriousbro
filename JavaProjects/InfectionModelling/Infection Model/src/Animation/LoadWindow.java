package Animation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;

/*
 * This is the LoadWindow class. This class serves to provide a GUI that allows the user to save while all of the save work is done in the InstantiateSvae class. Notice that this class
 * extends JDialog.
 */
public class LoadWindow extends JDialog implements ActionListener{

	//This is the active user's name
	private String user = System.getProperty("user.name");
	//This is the directory of the save folder
	private String FolderDir = "C:\\Users\\" + user + "\\Desktop\\Infection Spreading Saves";
	// This is a file created with the folder directory which will be used to check if the folder exists
	private File folder = new File(FolderDir);
	//This is a File[] that will contain all the saved files in the svae folder
	private File[] savedFiles;
	// this is a String[] that will have the names of all the saved files in the save folder
	private String[] fileNames;
	// This is a JComboBox that will allow the user to select a saved file
	private JComboBox loadFile;
	// This is the playPause ActionListener from the AnimationWindow Class which will be passed into the loaded graph
	private ActionListener pauseResume;

	/*
	 * This is the constructor for LoadWindow. This will initialize the File[] and the String[] as well as create an instance of this class that will spawn relative to 
	 * another window (because it is a JDiaolg class).
	 * @param accessedBy = the JFram which this will be created on top of
	 * @param pauseResume = the ActionListener from the AnimationWindow class that enables pausing
	 */
	public LoadWindow(JFrame accessedBy, ActionListener pauseResume) {
		this.pauseResume = pauseResume;
		savedFiles = folder.listFiles();
		fileNames = new String[savedFiles.length];
		for (int i = 0; i < fileNames.length; i++) {
			fileNames[i] = savedFiles[i].getName().substring(0, savedFiles[i].getName().length() - 4);
		}
		// These are the bounds for this class
		int width = 400;
		int height = 200;
		
		//This block of code initializes this class so it has components and is visible
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setSize(width, height);
		this.setLocationRelativeTo(accessedBy);
		this.setName("Load File");
		this.setResizable(false);
		this.setAutoRequestFocus(true);
		initialize();
	}

	/*
	 * This method initializes the components of this class.
	 */
	public void initialize() {
		// creates a label
		JLabel lbl = new JLabel("Load File?");
		lbl.setBounds(170, 0, 150, 20);
		this.add(lbl);

		// creates the combo box that the user can select a saved file from
		loadFile = new JComboBox(fileNames);
		loadFile.setBounds(7, 40, 380, 20);
		this.add(loadFile);

		// creates a JButton that will load the file that is selected. It adds this class as an ActionListener
		// look at the actionPerformed method to see what clicking this button does
		JButton load = new JButton("Load");
		load.setBounds(155, 145, 80, 20);
		load.addActionListener(this);
		this.add(load);
		//This allows the user to press enter to press the button
		this.getRootPane().setDefaultButton(load);

	}
	
	/*
	 * This method overrides that in the ActionListener class. This method will create a LoadData class that will fill the desired file and then obtain the Data class
	 * created and pass it into a Graph to display.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println((String) loadFile.getSelectedItem());
		
		// creates a LoadData class with the file name from the desired combo box
		LoadData load = new LoadData((String) loadFile.getSelectedItem());
		// bounds for the new Graph window
		int yBound = 250;
		int xBound = 600;
		// creates a new Graph that is loaded with Data returned from the LoadData.load() method
		Graph graph = new Graph(load.load(), xBound, yBound, true);
		// initializes the graph
		graph.initializeGraph(load.getLoadName(), 600, 700, xBound, yBound, pauseResume);
		// disposes this class so it is no longer visible
		this.dispose();
	}

}

/*
 * This was the main method I used to test this class.
 */
/*
 * public static void main(String[] args) { JFrame frm = new JFrame("Test");
 * frm.setBounds(0, 0, 500, 200); frm.setVisible(true); LoadWindow load = new
 * LoadWindow(frm); load.setVisible(true); }
 */
