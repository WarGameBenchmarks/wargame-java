import java.text.*;
import java.util.*;

class WarGameThread extends Thread {

	public int total = 0;
	public int processed = 0;
	public boolean terminate = false;

	public WarGameThread( int games  ) {
		super();
		this.total = games;
		start();
	}

	public void terminate() {
		this.terminate = true;
	}
	
	public void run() {
		int end = this.total;
		for (int i = 0; i < end && !this.terminate; i++) {
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
	
	public static void terminateThreads(WarGameThread[] threads) {
		for (WarGameThread t: threads) {
			t.terminate();
		}
	}

	public static long getTime() {
		return System.nanoTime();
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
		System.out.print("Enter number of threads (enter 0 for "+processors+"): ");
		int threads = scanner.nextInt();
		System.out.print("Enter precision [10^n] (0 for n=6): ");
		int games = scanner.nextInt();

		if ( 0 == threads ) threads = processors;
		if ( 0 == games ) games = 1000000;
		
		games = (int)Math.pow(10, games);
		
		WarGameThread[] ts = new WarGameThread[ threads ];

		long n = 0;
		int polling = (int)Math.pow(1 + (int)Math.log10(games), 3);
		double completed = 0;
		long current_time = 0;
		double speed = 0, last_speed = 0;
		double stability = 0, stability_threshold = 1000000;
		boolean stable = false;
		String str = "";

		long start = getTime();

		for (int i = 0; i < threads; i++) {
			ts[i] = new WarGameThread( (int)(games/threads) );
		}

		while ( isAlive(ts) ) {
			if ( (n++ % polling) != 0 ) continue;
			 completed = getCompleted(ts);
			 current_time = getTime() - start;

			 speed = current_time / completed;
			 if ( stable) {
				str = "(+)";
				terminateThreads(ts);
			 }
			 else if ( (speed - last_speed)/last_speed < 0.000005 ) {
				stability++;
				str = "(-)";
				if ( stability >= stability_threshold ) {
					stable = true;
				}
			 }
			 else str = "(X)";
			 last_speed = speed;
			 

			System.out.print("\r " + pf.format( stability / stability_threshold ) + " - Speed: " + df.format( speed / 1000000 ) + " (ms/g) " + str );
		}

		long end = getTime();

		System.out.print("\r " + pf.format( completed / games ) + "  -  Speed: " + df.format( speed / 1000000 ) + " (ms/g) " + str );

		System.out.println();
		System.out.println( "Elasped time: " + ((end - start) / 1000000) + " ms " );

	}

}


