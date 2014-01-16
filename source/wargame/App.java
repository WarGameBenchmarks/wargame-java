package wargame;

import java.text.*;

class App {

	public static final long ms = Benchmark.MS;
	public static final long ns = Benchmark.NS;
	public static final NumberFormat pf = NumberFormat.getPercentInstance();
	public static final NumberFormat df = NumberFormat.getInstance();
	public static final NumberFormat inf = NumberFormat.getIntegerInstance();
	
	private static boolean interrupt = false;

	public static void main(String[] args) {
		
		print_header();

		pf.setMaximumFractionDigits(2);
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(4);
		


		BenchmarkSettings settings = handleArguments(args);
		
		print_settings(settings);
		
		final Benchmark benchmark = new Benchmark(settings);
		BenchmarkPrinter printer = new BenchmarkPrinter(benchmark) {
			public void print() {
				String display_tail;
			 	if ( this.getBenchmark().isTest_started() ) display_tail = "Test #" + this.getBenchmark().getTests() + " at " + inf.format( (this.getBenchmark().getTest_time() - this.getBenchmark().getElapsed_time()) / ns  ) + " seconds";
			 	else display_tail = inf.format( (this.getBenchmark().getPrime_time() - this.getBenchmark().getElapsed_time()) / ns ) + " seconds left";

			 	System.out.print("\r " + " Speed: " + df.format( (this.getBenchmark().getSpeed() * ms) ) + " (g/ms) - " + display_tail );
			}
		};
		benchmark.attachPrinter(printer);
		
		Thread shutdown = new Thread() {

			public void run() {
				benchmark.end();
				System.out.println("\n\n\t--- Interrupt ---\n");
				interrupt = true;	
			}

		};

		Runtime.getRuntime().addShutdownHook(shutdown);

		benchmark.start();

		if ( interrupt == false ) {
			Runtime.getRuntime().removeShutdownHook(shutdown);
		}

		print_results(benchmark);

	}

	public static BenchmarkSettings handleArguments(String[] args) {
		boolean hasArguments = ( args.length > 0 );
		
		BenchmarkSettings settings = new BenchmarkSettings();
		
		
		if ( hasArguments && args[0].equals("i") ) {
			System.out.println( " --- Interactive Mode" );

			SettingsSelector ss = new SettingsSelector(settings);
			
			ss.getThreads();
			ss.getPrimeTime();
			ss.getVariationMagnitude();
			ss.getMaximumTests();
			ss.getUpdateFrequency();
			
		} else if ( hasArguments && args[0].equals("s") ) {
			System.out.println( " --- Short Run Mode" );
			
			settings.variation_magnitude(2).prime_time(10).update_frequency(30).maximum_tests(10);

		} else if ( hasArguments && (args[0].equals("h") || args[0].equals("help")) ) {
			print_help();
			System.exit(1);
		}
	
		return settings;
	
		
	}
	
	public static void print_results(Benchmark benchmark) {
		System.out.println();
		System.out.println( " Results:");
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
		
		if ( interrupt == false ) {
			System.out.println();
			System.out.println("  Submit results: \n\thttp://ifupdown.com/wg/results.php?fsc=" + inf.format( Math.round((benchmark.getSpeed() * ms)) ) + "&fsp=" + df.format( (benchmark.getSpeed() * ms) ) + "&tds=" + benchmark.getSettings().getAvailable_threads() + "&elt=" + inf.format((benchmark.getEnd_time() - benchmark.getStart_time()) / ns) + "&gcd=" + WarGameThread.getCompleted(benchmark.getThreads()));
		}

		System.out.println();
	}
	
	public static void print_help() {
		System.out.println( "  --- Help");
		System.out.println( "  ---  java App: runs with default settings");
		System.out.println( "  ---  java App s: runs a short test");
		System.out.println( "  ---  java App i: allows user to choose settings");
		System.out.println( "  ---  java App help: displays this information");
		System.out.println( "    --  threads: defaults to the number of logical processors available");
		System.out.println( "    --  variation magnitude: defaults to 4 e.g. 10^-4 or .0001, results will be +/- .01%");
		System.out.println( "    --  prime time: the period to wait before beginning testing");
		System.out.println( "    --  maximum tests: the maximum number of tests allowed per run");
		System.out.println( "    --  update frequency: the number of times per second values update");
		System.out.println( "  ---  Visit http://ifupdown.com/wg for more information...");
		System.out.println();
		System.out.println();
}

	public static void print_header() {
		System.out.println();
		System.out.println( "    =============================    " );
		System.out.println( "    =======  WarGame 2.1  =======    " );
		System.out.println( "    =============================    " );
		System.out.println();
	}

	public static void print_settings(BenchmarkSettings settings) {
		System.out.println();
		System.out.println( " Settings:");
		System.out.println( "  Number of threads: " + settings.getAvailable_threads() );
		System.out.println( "  Variation magnitude: " + settings.getVariation_magnitude() + " [" + pf.format(Math.pow(10, settings.getVariation_magnitude())) + "] " );
		System.out.println( "  Prime time: " + settings.getPrime_time() + " seconds " );
		System.out.println( "  Maximum tests: " + settings.getMaximum_tests() + " " );
		System.out.println( "  Update frequency: " + settings.getUpdate_frequency() + " fps " );
		System.out.println();
	}

}


