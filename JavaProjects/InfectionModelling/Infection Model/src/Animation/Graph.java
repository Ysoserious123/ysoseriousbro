package Animation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class Graph extends JPanel implements ActionListener, MouseMotionListener, MouseListener {

	public Timer timer;
	private int delay = 1;

	public JMenuItem playPause;

	public int x = 0;

	private int infectedHeight;
	private int healthyHeight;
	private int recoveredHeight;

	private int width;

	private Data data;

	private int maxY;
	public int maxX;

	private int mouseX = 0;
	private int mouseY = 0;
	public boolean mouseOnScreen = false;

	private boolean isLoaded;

	private volatile boolean paused = false;

	private String showData = "";
	private String legend = "Legend: (Healthy, Infected, Recovered)";
	// private boolean first = true;

	public Graph(Data d, int maxX, int maxY, boolean loaded) {
		isLoaded = loaded;
		data = d;
		width = 1;
		infectedHeight = data.getNumInfected();
		this.maxX = maxX;
		this.maxY = maxY;
		timer = new Timer(delay, this);
		timer.start();
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	public void initializeGraph(String name, int startX, int startY, int xBound, int yBound,
			ActionListener pauseResume) {

		JFrame frm = new JFrame(name);
		Cursor blank = Toolkit.getDefaultToolkit()
				.createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "invis");
		setCursor(blank);
		frm.add(this);

		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem save = new JMenuItem("Save Graph Data");
		JMenuItem load = new JMenuItem("Load Graph Data");
		playPause = new JMenuItem("Pause");
		playPause.setIcon(new ImageIcon("pause-button.png"));
		playPause.setBounds(20, 0, 30, 20);

		playPause.setMnemonic(KeyEvent.VK_P);
		playPause.addActionListener(pauseResume);
		playPause.setPreferredSize(new Dimension(80, (int) playPause.getPreferredSize().getHeight()));

		class SaveActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {

				if (!timer.isRunning()) {

					SaveWindow save = new SaveWindow(name, frm, data);
					save.setVisible(true);

				} else {
					JOptionPane.showMessageDialog(null, "Cannot save while program is running", "Save Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		class LoadActionListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (!getTimer().isRunning()) {
					LoadWindow load = new LoadWindow(frm, pauseResume);
					load.setVisible(true);
				}
			}
		}

		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		load.addActionListener(new LoadActionListener());

		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		save.addActionListener(new SaveActionListener());

		file.add(save);
		file.add(load);

		frm.setBounds(startX, startY, xBound, yBound);
		frm.setFocusable(true);
		frm.setResizable(false);
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		menuBar.add(file);
		menuBar.add(playPause);
		frm.setJMenuBar(menuBar);
		// frm.add(graph);
		frm.setVisible(true);
	}

	public JMenuItem getPlayPause() {
		return playPause;
	}

	public void pause() {
		timer.stop();
	}

	public void play() {
		timer.restart();
	}

	public Timer getTimer() {
		return timer;
	}

	public void paintComponent(Graphics g) {// throws IndexOutOfBoundsException{
		super.paintComponent(g);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 700, 300);

		for (int i = 0; i < data.getInfectedPercentage().size(); i++) {

			infectedHeight = (int) ((maxY - 55) * data.getInfectedPercentage().get(i));
			healthyHeight = (int) ((maxY - 55) * data.getHealthyPercentage().get(i));
			recoveredHeight = (int) ((maxY - 55) * data.getRecoveredPercentage().get(i));

			g.setColor(Color.MAGENTA);
			g.fillRect(width * i, maxY - 55, width, -infectedHeight);
			g.setColor(Color.CYAN);
			g.fillRect(width * i, (maxY - 55) - infectedHeight, width, -healthyHeight);
			g.setColor(Color.GREEN);
			g.fillRect(width * i, ((maxY - 55) - infectedHeight) - healthyHeight, width, -recoveredHeight);

		}

		g.setColor(Color.BLACK);

		if (mouseOnScreen) {
			g.drawLine(mouseX - 5, mouseY, mouseX + 5, mouseY);
			g.drawLine(mouseX, mouseY - 5, mouseX, mouseY + 5);

			g.drawString(showData, mouseX + 4, mouseY + 10);

		}

		g.drawString(legend, 0, 10);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

		// The program is set up to not restart the timer here to allow for pauses but a
		// loaded file must restart its timer by itself
		if (isLoaded) {
			timer.restart();
			repaint();
		} else {
			if (!paused) {
				x = x + width;
				data.updateData();

				if (x == maxX - 20 && data.getNumInfected() == 0) {
					timer.stop();
				} else if (x == maxX - 20) {
					data.resize();
					try {
						showData = "(" + data.getHealthy().get(mouseX - 1) + ", " + data.getInfected().get(mouseX - 1)
								+ ", " + data.getRecovered().get(mouseX - 1) + ")";
					} catch (IndexOutOfBoundsException e) {
						// do nothing (keep string as is)
					}
					x = (maxX / 2) + 80;
				}
				repaint();
			}
		}
	}

	public boolean canRun() {
		return !(x == maxX - 20 && data.getNumInfected() == 0);
	}

	public Data getData() {
		return data;
	}

	public void setPaused(boolean pause) {
		paused = pause;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		mouseX = arg0.getX();
		mouseY = arg0.getY();
		
		try {
			showData = "(" + data.getHealthy().get(mouseX - 1) + ", " + data.getInfected().get(mouseX - 1) + ", "
					+ data.getRecovered().get(mouseX - 1) + ")";
		} catch (IndexOutOfBoundsException e) {
			// do nothing (keep string as is)
		}

		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseOnScreen = true;
		if (mouseX < x) {
			showData = "(" + data.getHealthy().get(mouseX) + ", " + data.getInfected().get(mouseX) + ", "
					+ data.getRecovered().get(mouseX) + ")";
		}

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseOnScreen = false;

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
