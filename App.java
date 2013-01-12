import java.text.*;
import java.util.*;

class WarGameThread extends Thread {

	public int total = 0;
	public int processed = 0;

	public WarGameThread( int games  ) {
		super();
		this.total = games;
		start();
	}

	public void run() {
		
		for (int i = 0; i < this.total; i++) {
			this.processed = i;
			WarGame wg = new WarGame();
		}	

	}

}

class App {



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

	public static synchronized int getCompleted(WarGameThread[] threads) {
		int processed = 0;
		for (WarGameThread t : threads ) {
			processed = processed + t.processed;
		}

		return processed;
	}

	public static long getTime() {
		return System.nanoTime() / 1000000;
	}

	public static void main(String[] args) throws Exception {
		
		NumberFormat pf = NumberFormat.getPercentInstance();
		pf.setMaximumFractionDigits(2);
		pf.setMinimumFractionDigits(2);
		pf.setMaximumIntegerDigits(3);
		pf.setMinimumIntegerDigits(2);
		NumberFormat df = NumberFormat.getInstance();
		df.setMaximumFractionDigits(3);
		df.setMinimumFractionDigits(3);

		Scanner scanner = new Scanner(System.in);

		int processors = Runtime.getRuntime().availableProcessors();
		System.out.print("Enter number of threads ("+processors+"): ");
		int threads = scanner.nextInt();
		System.out.print("Enter number of iterations (games): ");
		int games = scanner.nextInt();

		WarGameThread[] ts = new WarGameThread[ threads ];

		long n = 0;
		int polling = (int)Math.pow(1 + (int)Math.log10(games), 2);
		double completed = 0;
		long current_time = 0;
		double speed = 0;
		double remaining = 0;

		long start = getTime();

		for (int i = 0; i < threads; i++) {
			ts[i] = new WarGameThread( (int)(games/threads) );
		}

		while ( isAlive(ts) ) {
			if ( (n++ % polling) != 0 ) continue;
			 completed = getCompleted(ts); // games completed, found through asking the threads
			 current_time = getTime() - start; 

			 speed = current_time / completed;

			System.out.print("\r " + pf.format( completed / games ) + "  -  Speed: " + df.format( speed ) + " (ms/g) " );
		}

		for (int i = 0; i < threads; i++) {
			ts[i].join();
		}
		long end = getTime();

		System.out.print("\r " + pf.format( 1 ) + "  -  Speed: " + df.format( speed ) + " (ms/g) " );

		System.out.println();
		System.out.println( "Elasped time: " + (end - start) + " ms " );

	}

}


