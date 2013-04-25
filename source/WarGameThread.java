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

	public static void launch(WarGameThread[] threads) {
		for (WarGameThread t: threads) {
			t.start();
		}	
	}

	public static void terminateThreads(WarGameThread[] threads) {
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
		while ( !this.terminate ) {
			this.processed++;
			WarGame wg = new WarGame();
		}	

	}

}