import java.text.*;
import java.util.*;

class SettingsSelector {
	
	private Scanner scanner = new Scanner(System.in);
	private BenchmarkBuilder settings;
	
	SettingsSelector(BenchmarkBuilder bb) {
		this.settings = bb;
	}
	
	public void getThreads() {
		int temp = 0;
		System.out.print( "  -- Enter number of threads\n   (enter 0 for " + this.settings.getAvailable_threads() + " threads): " );
		temp = scanner.nextInt();
		if ( 0 != temp ) {
			this.settings.available_threads(temp);
		}
	}
	
	public void getPercentVariation() {
		int temp = 0;
		System.out.print( "  -- Enter confidence threshold [10^-n]\n   (enter 0 for " + this.settings.getVariation_magnitude() + "): " );
		temp = scanner.nextInt();
		if ( 0 != temp ) {
			this.settings.variation_magnitude(temp);
		}
	}
	
	public double computePercentVariation() {
		return (double)(Math.pow(10, -1 * this.settings.getVariation_magnitude()));
	}

	
	public void getPrimeTime() {
		int temp = 0;
		System.out.print( "  -- Enter priming time\n   (enter 0 for " + this.settings.getPrime_time() + " seconds): " );
		temp = scanner.nextInt();
		if ( 0 != temp ) {
			this.settings.prime_time(temp);
		}
	}
	
	public long computePrimeTime(int time) {
		return Benchmark.NS * (long)time;
	}
	
	
	
	
}

class App {

	public static void print_help() {
			System.out.println( "  --- Help");
			System.out.println( "  ---  java App: runs with default settings");
			System.out.println( "  ---  java App s: runs a short test");
			System.out.println( "  ---  java App i: allows user to choose settings");
			System.out.println( "  ---  java App help: displays this information");
			System.out.println( "    --  threads: defaults to the number of logical processors available");
			System.out.println( "    --  confidence threshold: defaults to 00.01%, results will be +/- .01%");
			System.out.println( "    --  prime time: the period to wait before beginning testing");
			System.out.println( "  ---  Visit http://ifupdown.com/wg for more information...");
			System.out.println();
			System.out.println();
	}

	public static void print_header() {
		System.out.println();
		System.out.println( "    =============================    " );
		System.out.println( "    ====  WarGame 2 | V 1.1  ====    " );
		System.out.println( "    =============================    " );
		System.out.println();
	}

	public static void initialize(String[] args) {
		boolean hasArguments = ( args.length > 0 );
		
		
		
		if ( args.length > 0 && args[0].equals("i") ) {


		} else if ( args.length > 0 && args[0].equals("s") ) {
			System.out.println( " --- Short Run Mode" );
			percent_variation = (double)(Math.pow(10, -1 * 2));
			prime_time = ns * (long)10;

		} else if ( args.length > 0 && (args[0].equals("h") || args[0].equals("help")) ) {
			print_help();
			System.exit(1);
		}
		
		
		
		
		
		
		
	}

	public static void main(String[] args) throws Exception {
		
		print_header();

		final NumberFormat pf = NumberFormat.getPercentInstance();
		final NumberFormat df = NumberFormat.getInstance();
		final NumberFormat inf = NumberFormat.getIntegerInstance();

		pf.setMaximumFractionDigits(2);
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(4);


		final long ms = 1000000L;
		final long ns = 1000000000L;


		int threads = Runtime.getRuntime().availableProcessors();
		double percent_variation = 0.0001;
		long prime_time = 60 * ns;



		System.out.println();
		System.out.println( "  Using " + threads + " for number of threads." );
		System.out.println( "  Using " + Math.abs(Math.log10(percent_variation)) + " [" + pf.format(percent_variation) + "] for confidence threshold." );
		System.out.println( "  Using " + inf.format(prime_time / ns) + " seconds for prime time." );
		System.out.println();
				
		
		BenchmarkBuilder settings = new BenchmarkBuilder(threads, percent_variation, prime_time);
		Benchmark benchmark = new Benchmark(settings);
		BenchmarkPrinter printer = new BenchmarkPrinter(benchmark) {
			public void print() {
				String display_tail;
			 	if ( this.getBenchmark().isTest_started() ) display_tail = "Test #" + this.getBenchmark().getTests() + " at " + inf.format( (this.getBenchmark().getTest_time() - this.getBenchmark().getElapsed_time()) / ns  ) + " seconds";
			 	else display_tail = inf.format( (this.getBenchmark().getSettings().getPrime_time() - this.getBenchmark().getElapsed_time()) / ns ) + " seconds left";

			 	System.out.print("\r " + " Speed: " + df.format( (this.getBenchmark().getSpeed() * ms) ) + " (g/ms) - " + display_tail );
			}
		};
		benchmark.attachPrinter(printer);
		
		benchmark.start();
		

		System.out.println();
		System.out.println();
				
		System.out.println( "  Elapsed time: \n\t" +  inf.format((benchmark.getEnd_time() - benchmark.getStart_time()) / ns) + " seconds \n\t~" + df.format((benchmark.getEnd_time() - benchmark.getStart_time()) / ms / 1000.0 / 60.0) + " minutes");
		System.out.println( "  Games completed: \t" + inf.format( WarGameThread.getCompleted(benchmark.getThreads()) ) + " " );
		System.out.println( "  Tests completed: \t" + inf.format( benchmark.getTests() ) + " " );

		System.out.println();
		
		System.out.println( "  Speed improvement: " + pf.format( 1 - (benchmark.getPrime_speed() / benchmark.getSpeed()) ) + "");
		System.out.println( "   from " + df.format(benchmark.getPrime_speed() * ms) + " (g/ms) to " + df.format(benchmark.getSpeed() * ms) + " (g/ms)");
		System.out.println( "  Final confidence range:\n   " + df.format(1/benchmark.getRate_low() * ms) + " (g/ms) > " + df.format(benchmark.getSpeed() * ms) + " (g/ms) > " + df.format(1/benchmark.getRate_high() * ms) + " (g/ms)" );

		System.out.println();

		System.out.println( "  Final rate: \t" + df.format( (benchmark.getRate() / ms) ) + " (ms/g)" );
		System.out.println( "  Final speed: \t" + df.format( (benchmark.getSpeed() * ms) ) + " (g/ms)" );
		System.out.println();
		System.out.println( "  Final score: \t" + inf.format( Math.round((benchmark.getSpeed() * ms)) ) + " " );
		
		System.out.println();
	}

}


