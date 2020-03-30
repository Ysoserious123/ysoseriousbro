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


public class LoadData {
	
	private String loadName;
	private static String user = System.getProperty("user.name");
	private static String dir = "C:\\Users\\" + user + "\\Desktop\\Infection Spreading Saves\\";
	private String CSVdir;
	private Scanner sc;
	
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
	
	public Data load() {
		ArrayList<Integer> infected = new ArrayList<Integer>();
		ArrayList<Integer> healthy = new ArrayList<Integer>();
		ArrayList<Integer> recovered = new ArrayList<Integer>();
		sc.nextLine(); // Every saved file has a header which we do not want so we skip it
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
