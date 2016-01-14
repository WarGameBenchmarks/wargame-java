package wargame;

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

	public static WarGameThread[] create(int threads, int base) {
		WarGameThread[] wgThreads = new WarGameThread[threads];
		for (int i = 0; i < threads; i++) {
			wgThreads[i] = new WarGameThread(base);
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
	public int base = 0;

	public WarGameThread(int base) {
		super();
		this.base = base;
	}

	public void terminate() {
		this.terminate = true;
	}

	public void run() {
		WarGame game = new WarGame();

		while ( !this.terminate ) {

			for (int i = 0; i < base; i++) {
				game.play();
			}
			this.processed += base;

		}

	}

}
