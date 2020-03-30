package Animation;

import javax.swing.*;

public interface PauseResume {
    
    public Object lock = new Object();
    boolean paused = true;
    
    public PauseResume() {
        counter.start();
    }
    
    public Object getLock() {
    	return lock;
    }
    
    public Thread counter = new Thread(new Runnable() {
		@Override
		public void run() {
			if (true) {
				work();
			}
		}
	});

	public Object getLock() {
		return lock;
	}

	public Thread getCounter() {
		return counter;
	}

	private void work() {
		if (canRun() && graph.canRun()) {
			allowPause();
			timer.restart();
			graph.timer.restart();
			for (int i = 0; i < people.length; i++) {
				people[i].pause(false);
			}
		}
		done();
	}

	private void allowPause() {
		synchronized (lock) {
			try {
				if (paused) {
					timer.stop();
					graph.timer.stop();
				}
				lock.wait();
			} catch (InterruptedException e) {
				// nothing
				System.out.println("could not stop");
			}
		}
	}

	private java.awt.event.ActionListener pauseResume = new java.awt.event.ActionListener() {
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			paused = !paused;

			if (paused) {
				play.setIcon(pauseImg);
				for (int i = 0; i < people.length; i++) {
					people[i].pause(true);
				}
			} else {
				play.setIcon(playImg);
				for (int i = 0; i < people.length; i++) {
					people[i].pause(false);
				}
			}

			synchronized (lock) {
				lock.notifyAll();
			}
		}
	};

	private void sleep() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// nothing
			System.out.println("WHOOPS");
		}
	}

	private void done() {
		// button.setText("Start");
		paused = true;
	}