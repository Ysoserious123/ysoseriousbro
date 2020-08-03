package Animation;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/*
 * This is the InstantiateSave class. Its job is to save the data displayed on the graph into a .csv file to be opened in excel or another program to 
 * analyze the data more thoroughly. It assumes that the user has a desktop to create a folder on to save the data in upon the first run of this program and that the directory for the desktop
 * is C:\Users\active_user\Desktop. Eventually I plan to make this more universal.
 */
public class InstantiateSave {

	// Note: anything static in these classes is so the Menu can add the folder to the desktop if needed upon running the program without creating an instance of this class since nothing is being saved.
	// These are Strings that will be used to create the save folder and eventually the save files. System.getProperty("user.name") will return the name of the active user
	private static String user = System.getProperty("user.name");
	private static String dir = "C:\\Users\\" + user + "\\Desktop";
	
	// This is the directory for the .csv file that will be saved
	private String CSVdir;
	
	// The file that the data will be written to
	private File file;
	
	// This is the first line of every .csv file which labels the columns of data
	private final String HEADER = "Index,Healthy,Infected,Recovered";
	
	// The data class whose information will be written to the .csv file
	private Data data;
	
	// PrintWriter that will write the data to the file
	private PrintWriter pw;
	
	// The name of the save folder
	private final static String FOLDER_NAME = "\\Infection Spreading Saves";
	
	// A File class for the folder which will mainly be used to create the folder if it does not exist and will check to see if the folder is there
	private static File folder = new File(dir + FOLDER_NAME);
	
	// This is used when a file name already exists. Like Windows does, this number will be added to the end og the file name to ensure that files are not accidentally overwritten
	private int versionNumber = 1;
	
	// Name of the .csv file
	private String fileName;
	
	// The window that is trying to save the file. Useful for displaying the Overwrite window
	private Window saver;

	/*
	 * This is the constructor for InstantiateSave. This constructor is no longer in use so I will not explain it.
	 */
	public InstantiateSave(Data importedData, String name) {
		data = importedData;
		CSVdir = dir + FOLDER_NAME + "\\" + name + ".csv";

		file = new File(CSVdir);
		while (file.exists()) {
			//System.out.println("File Exists");
			name = name + versionNumber;
			CSVdir = dir + FOLDER_NAME + "\\" + name + ".csv";
			file = new File(CSVdir);
			versionNumber++;
		}
		fileName = name;
		file.delete();
	}
	
	/*
	 * This is the constructor for InstantiateSave. It initializes global variable not already initialized as well as renames the file using the versionNumber variable
	 * if necessary. 
	 * @param importedData = the Data class that is going to have its information saved to a file
	 * @param name = the name of the Graph being saved which will in turn be used as the file name
	 * @param windowTryingToSave = Window that the overwrite option will display on top of. For this program it will be the SaveWindow class.
	 */
	public InstantiateSave(Data importedData, String name, Window windowTryingToSave) {
		data = importedData;
		saver = windowTryingToSave;
		
		// sets the full directory for the file
		CSVdir = dir + FOLDER_NAME + "\\" + name + ".csv";

		// This block of code determines if a file at the CSVdir exists. If so, The file will be renamed as to not overwrite it accidentally
		file = new File(CSVdir);
		while (file.exists()) {
			//System.out.println("File Exists");
			CSVdir = dir + FOLDER_NAME + "\\" + name + versionNumber + ".csv";
			file = new File(CSVdir);
			versionNumber++;
		}
		
		// this gets the file name from the directory created
		fileName = CSVdir.substring((dir.length())+FOLDER_NAME.length()+1, CSVdir.length()-4);
		
		// The test file is no longer needed and is therefore deleted
		file.delete();
	}

	/*
	 * Returns if the save folder can be added to the desktop and simultaneously does so.
	 * @return true if file could be added and therefore was added
	 * @return false if file could not be created at the desired directory
	 */
	public static boolean CanAddFolder() {
		return folder.mkdir();
	}

	/*
	 * Returns of the directory for the file is valid.
	 * @return true if the save folder exists
	 * @return false if the save folder does not exist
	 */
	public static boolean doesFolderExist() {
		return folder.isDirectory();
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String newName) {
		fileName = newName;
	}
	
	/*
	 * Returns a JDialog class that will ask the user if they wish to overwrite the file or not.
	 * @param tempFile = the file name that exists and requires permission to overwrite
	 * @return JDialog overwrite
	 */
	public JDialog overWrite(String tempFile) {
		
		// this block of code initializes the overwrite JDialog window that will be created on top of the Window saver and cannot be clicked off of unless interacted with.
		JDialog overwrite = new JDialog(saver);
		overwrite.setModal(true);
		overwrite.getContentPane().setLayout(null);
		overwrite.setResizable(false);
		overwrite.setSize(400, 100);
		overwrite.setLocationRelativeTo(saver);
		overwrite.setName("Save File");
		overwrite.setResizable(false);
		overwrite.setAutoRequestFocus(true);

		// adds a label describing why the window is there
		JLabel lbl = new JLabel("File already exists. Would you like to overwrite?");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		lbl.setBounds(50, 0, 300, 20);
		overwrite.add(lbl);
		
		// creates an ActionListener for the yes JButton that will overwrite the file that already exists
		class YesButton implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// try to write to the file and get rid of the JDialog and the Window saver. If not, there is an error message
				try {
					CSVdir = tempFile;
					writeFile();
					overwrite.dispose();
					saver.dispose();
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "Could not overwrite file", "Save Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}

		// creates an adds the yes JButton to the JDialog with its ActionListener
		JButton yes = new JButton("Yes");
		yes.setBounds(110, 30, 80, 20);
		yes.addActionListener(new YesButton());
		overwrite.add(yes);
		
		// creates an ActionListener for the no JButton that will simply dispose of the JDialog
		class NoButton implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				overwrite.dispose();
			}
			
		}

		// creates and adds the no JButton to the JDialog with its ActionListener
		JButton no = new JButton("No");
		no.setBounds(210, 30, 80, 20);
		no.addActionListener(new NoButton());
		overwrite.add(no);
		return overwrite;
	}
	
	/*
	 * This method will attempt to save the file. If it cannot an overwrite window will appear. To do this, a temporary file is created and first it is checked
	 * to see if it already exists. If it doesn;t exist the file will be written. 
	 * @param checkFile = the file that may need to be overwritten
	 * @return true if the file could be saved
	 * @return false if the file could not be saved
	 */
	public boolean save(String checkFile) {
		
		String tempCSVdir = dir + FOLDER_NAME + "\\" + checkFile + ".csv";
		//System.out.println(tempCSVdir);
		file = new File(tempCSVdir);
		if (file.exists()) {// && saver != null) {
			overWrite(tempCSVdir).setVisible(true);
			return false;
		}else {
			try {
				writeFile();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Could not save file", "Save Error",
						JOptionPane.ERROR_MESSAGE);
			}
			return true;
		}
	}
	
	/*
	 * This method writes the data to the file. separating the data with commas hence the .csv file.
	 */
	public void writeFile() throws FileNotFoundException {
		pw = new PrintWriter(CSVdir);
		pw.println(HEADER);
		for (int i = 0; i < data.getInfected().size(); i++) {
			pw.println(i + "," + data.getHealthy().get(i) + "," + data.getInfected().get(i) + ","
					+ data.getRecovered().get(i));
		}
		pw.close();
	}

	/*
	 * Returns if a file exists.
	 * @return true if the global variable file exists
	 * @return false if the global variable file does not exist
	 */
	public boolean doesFileExist() {
		return file.isDirectory();
	}
	
	/*
	 * This was a main method that I used to test this class.
	 */
	
	/*public static void main(String[] args) {
		JFrame test = new JFrame();
		test.setBounds(200, 500, 600, 700);
		test.setVisible(true);
		Person[] people = new Person[10];
		Data data = new Data(people);
		InstantiateSave save = new InstantiateSave(data, "hello", test);
		save.overWrite().setVisible(true);
	}*/


}
