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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class InstantiateSave {

	private static String user = System.getProperty("user.name");
	private static String dir = "C:\\Users\\" + user + "\\Desktop";
	private String CSVdir;
	private File file;
	private final String HEADER = "Index,Healthy,Infected,Recovered";
	private Data data;
	private PrintWriter pw;
	private final static String FOLDER_NAME = "\\Infection Spreading Saves";
	private static File folder = new File(dir + FOLDER_NAME);
	private int versionNumber = 1;
	private String fileName;
	private Window saver;

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
	
	public InstantiateSave(Data importedData, String name, Window windowTryingToSave) {
		data = importedData;
		saver = windowTryingToSave;
		CSVdir = dir + FOLDER_NAME + "\\" + name + ".csv";

		file = new File(CSVdir);
		while (file.exists()) {
			//System.out.println("File Exists");
			CSVdir = dir + FOLDER_NAME + "\\" + name + versionNumber + ".csv";
			file = new File(CSVdir);
			versionNumber++;
		}
		fileName = CSVdir.substring((dir.length())+FOLDER_NAME.length()+1, CSVdir.length()-4);
		file.delete();
	}

	public static boolean CanAddFolder() {
		return folder.mkdir();
	}

	public static boolean doesFolderExist() {
		return folder.isDirectory();
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String newName) {
		fileName = newName;
	}
	
	public JDialog overWrite(String tempFile) {
		JDialog overwrite = new JDialog(saver);
		overwrite.setModal(true);
		overwrite.getContentPane().setLayout(null);
		overwrite.setResizable(false);
		overwrite.setSize(400, 100);
		overwrite.setLocationRelativeTo(saver);
		overwrite.setName("Save File");
		overwrite.setResizable(false);
		overwrite.setAutoRequestFocus(true);

		JLabel lbl = new JLabel("File already exists. Would you like to overwrite?");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		lbl.setBounds(50, 0, 300, 20);
		overwrite.add(lbl);
		
		class YesButton implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					CSVdir = tempFile;
					writeFile();
					overwrite.dispose();
					saver.dispose();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}

		JButton yes = new JButton("Yes");
		yes.setBounds(110, 30, 80, 20);
		yes.addActionListener(new YesButton());
		overwrite.add(yes);
		
		class NoButton implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				overwrite.dispose();
			}
			
		}

		JButton no = new JButton("No");
		no.setBounds(210, 30, 80, 20);
		no.addActionListener(new NoButton());
		overwrite.add(no);
		return overwrite;
	}
	
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
				e.printStackTrace();
			}
			return true;
		}
	}

	public void writeFile() throws FileNotFoundException {
		pw = new PrintWriter(CSVdir);
		pw.println(HEADER);
		for (int i = 0; i < data.getInfected().size(); i++) {
			pw.println(i + "," + data.getHealthy().get(i) + "," + data.getInfected().get(i) + ","
					+ data.getRecovered().get(i));
		}
		pw.close();
	}

	public boolean doesFileExist() {
		return file.isDirectory();
	}
	
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
