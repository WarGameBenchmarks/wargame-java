class WarGameThread extends Thread {

	public int processed = 0;
	public boolean terminate = false;

	public WarGameThread() {
		super();
		start();
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