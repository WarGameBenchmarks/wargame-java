package wargame;

import java.util.*;

class WarGameThread extends Thread {

	public static boolean isAlive(WarGameThread[] threads) {
		boolean life = false;
		for (WarGameThread t : threads) {
			if (t.isAlive()) {
				life = true;
				break;
			}
		}
		return life;
	}

	public static int getCompleted(WarGameThread[] threads) {
		int processed = 0;
		for (WarGameThread t : threads ) {
			processed = processed + t.processed;
		}

		return processed;
	}

	public static WarGameThread[] create(int threads) {
		WarGameThread[] wgThreads = new WarGameThread[threads];
		for (int i = 0; i < threads; i++) {
			wgThreads[i] = new WarGameThread();
		}
		return wgThreads;
	}

	public static void launch(WarGameThread[] threads) {
		for (WarGameThread t: threads) {
			t.start();
		}
	}

	public static void end(WarGameThread[] threads) {
		for (WarGameThread t: threads) {
			t.terminate();
		}
	}

	public int processed = 0;
	public boolean terminate = false;

	public WarGameThread() {
		super();
	}

	public void terminate() {
		this.terminate = true;
	}

	public void run() {
		WarGame game = new WarGame();
		Random rng = new Random();
		while ( !this.terminate ) {
			game.play(rng);
			this.processed++;
		}

	}

}
