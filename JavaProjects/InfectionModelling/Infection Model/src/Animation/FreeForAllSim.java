package Animation;

import java.util.Random;

import javax.swing.JFrame;

public class FreeForAllSim {
	public static void main(String[] args) {
		
		int xBound = 1000;
		int yBound = 700;
		int numPeople = 300;
		int movementSpeed = 1;
		int percentDistanced = 0;
		int radius = 2;
		
		Random rnd = new Random();
		
		JFrame frm = new JFrame("COVID-19 (Free-For-All)");
		
		Window win = new Window(numPeople, radius, xBound, yBound, rnd, movementSpeed, percentDistanced);
		
		frm.setBounds(0, 0, xBound, yBound);
		frm.setFocusable(true);
		frm.setResizable(false);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frm.add(win);
		frm.setVisible(true);
		frm.add(win);
	}

}
