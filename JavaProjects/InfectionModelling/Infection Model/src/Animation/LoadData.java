package Animation;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/*
 * This is the LoadData class. Its job is to fill a Data class with data saved in a .csv file
 */
public class LoadData {
	
	// This is the name of the loaded file
	private String loadName;
	
	// This is the username of the active user
	private String user = System.getProperty("user.name");
	// This is the directory of the save folder created specifically for this program
	private String dir = "C:\\Users\\" + user + "\\Desktop\\Infection Spreading Saves\\";
	// This is the complete directory for the .csv file
	private String CSVdir;
	// this is the Scanner class that will read the file
	private Scanner sc;
	
	/*
	 * This is the constructor for LoadData. It initializes the global variable that have not been initialized. This creates a Scanner class to read the load file. If it can't
	 * an error message appears.
	 * @param filename = name of file being loaded
	 */
	public LoadData(String fileName) {
		loadName = fileName;
		CSVdir = dir + fileName + ".csv";
		File loadFile = new File(CSVdir);
		try {
			sc = new Scanner(loadFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "File Does Not Exist", "Load Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/*
	 * This returns a Data class with all of its necessary storage containers filled from the save file.
	 * @return Data filled from loaded file
	 */
	public Data load() {
		ArrayList<Integer> infected = new ArrayList<Integer>();
		ArrayList<Integer> healthy = new ArrayList<Integer>();
		ArrayList<Integer> recovered = new ArrayList<Integer>();
		sc.nextLine(); // Every saved file has a header which we do not want so we skip it
		
		// This block of code creates a string array from each line of the load file and puts each datum in an array index and then into the
		// appropriate storage container.
		while(sc.hasNextLine()) {
			String[] info = sc.nextLine().split(",");
			healthy.add(Integer.parseInt(info[1]));
			infected.add(Integer.parseInt(info[2]));
			recovered.add(Integer.parseInt(info[3]));
		}
		
		return new Data(infected, healthy, recovered);
	}
	
	public String getLoadName() {
		return loadName;
	}

}
