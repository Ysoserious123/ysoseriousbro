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

public class LoadWindow extends JDialog implements ActionListener{

	private static String user = System.getProperty("user.name");
	private static String FolderDir = "C:\\Users\\" + user + "\\Desktop\\Infection Spreading Saves";
	private File folder = new File(FolderDir);
	private File[] savedFiles;
	private String[] fileNames;
	private JComboBox loadFile;
	private ActionListener pauseResume;

	public LoadWindow(JFrame accessedBy, ActionListener pauseResume) {
		this.pauseResume = pauseResume;
		savedFiles = folder.listFiles();
		fileNames = new String[savedFiles.length];
		for (int i = 0; i < fileNames.length; i++) {
			fileNames[i] = savedFiles[i].getName().substring(0, savedFiles[i].getName().length() - 4);
		}
		int width = 400;
		int height = 200;
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setSize(width, height);
		this.setLocationRelativeTo(accessedBy);
		this.setName("Save File");
		this.setResizable(false);
		this.setAutoRequestFocus(true);
		initialize();
	}

	public void initialize() {
		JLabel lbl = new JLabel("Load File?");
		lbl.setBounds(170, 0, 150, 20);
		this.add(lbl);

		loadFile = new JComboBox(fileNames);
		loadFile.setBounds(7, 40, 380, 20);
		this.add(loadFile);

		JButton load = new JButton("Load");
		load.setBounds(155, 145, 80, 20);
		load.addActionListener(this);
		this.add(load);
		this.getRootPane().setDefaultButton(load);

	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println((String) loadFile.getSelectedItem());
		LoadData load = new LoadData((String) loadFile.getSelectedItem());
		int yBound = 250;
		int xBound = 600;
		Graph graph = new Graph(load.load(), xBound, yBound, true);
		graph.initializeGraph(load.getLoadName(), 600, 700, xBound, yBound, pauseResume);
		this.dispose();
	}

}
/*
 * public static void main(String[] args) { JFrame frm = new JFrame("Test");
 * frm.setBounds(0, 0, 500, 200); frm.setVisible(true); LoadWindow load = new
 * LoadWindow(frm); load.setVisible(true); }
 */
