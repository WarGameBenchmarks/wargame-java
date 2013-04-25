import java.text.*;
import java.util.*;

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
		System.out.println( "    =====================    " );
		System.out.println( "    ====  WarGame 2  ====    " );
		System.out.println( "    =====           =====    " );
		System.out.println( "    ===   V 1.0.1.0   ===    " );
		System.out.println( "    =====================    " );
		System.out.println();
	}

	

	public static void main(String[] args) throws Exception {
		
		print_header();

		NumberFormat pf = NumberFormat.getPercentInstance();
		NumberFormat df = NumberFormat.getInstance();
		NumberFormat inf = NumberFormat.getIntegerInstance();

		pf.setMaximumFractionDigits(2);
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(4);


		final long ms = 1000000L;
		final long ns = 1000000000L;


		int threads = Runtime.getRuntime().availableProcessors();
		double percent_variation = 0.0001;
		long prime_time = 60 * ns;

		if ( args.length > 0 && args[0].equals("i") ) {
			Scanner scanner = new Scanner(System.in);
			int temp;

			System.out.println( " --- Interactive Mode" );

			System.out.print( "  -- Enter number of threads\n   (enter 0 for " + threads + " threads): " );
			temp = scanner.nextInt();
			if ( 0 != temp ) threads = temp;
			

			System.out.print( "  -- Enter confidence threshold [10^-n]\n   (enter 0 for " + Math.abs(Math.log10(percent_variation)) + "): " );
			temp = scanner.nextInt();
			if ( 0 != temp ) {
				percent_variation = (double)(Math.pow(10, -1 * temp));
				if (temp > 4) pf.setMaximumFractionDigits( temp + 1 );
			}
			

			System.out.print( "  -- Enter priming time\n   (enter 0 for " + inf.format(prime_time / ns) + " seconds): " );
			temp = scanner.nextInt();
			if ( 0 != temp ) prime_time = ns * (long)temp;

		} else if ( args.length > 0 && args[0].equals("s") ) {
			System.out.println( " --- Short Run Mode" );
			percent_variation = (double)(Math.pow(10, -1 * 2));
			prime_time = ns * (long)10;

		} else if ( args.length > 0 && (args[0].equals("h") || args[0].equals("help")) ) {
			print_help();
			System.exit(1);
		}

		System.out.println();
		System.out.println( "  Using " + threads + " for number of threads." );
		System.out.println( "  Using " + Math.abs(Math.log10(percent_variation)) + " [" + pf.format(percent_variation) + "] for confidence threshold." );
		System.out.println( "  Using " + inf.format(prime_time / ns) + " seconds for prime time." );
		System.out.println();
				

		BenchmarkSettings settings = new BenchmarkSettings(threads, percent_variation, prime_time);
		Benchmark benchmark = new Benchmark(settings);


		System.out.print("\r " + " Speed: " + df.format( (speed * ms) ) + " (g/ms) - " + display_tail );

		System.out.println();
		System.out.println();
				
		System.out.println( "  Elapsed time: \n\t" + 
			inf.format((end - start) / ns) + " seconds \n\t~" + df.format((end - start) / ms / 1000.0 / 60.0) + " minutes");
		System.out.println( "  Games completed: \t" + inf.format( getCompleted(wgts) ) + " " );
		System.out.println( "  Tests completed: \t" + inf.format( tests ) + " " );

		System.out.println();
		
		System.out.println( "  Speed improvement: " + pf.format( 1 - (prime_speed / speed) ) + "");
		System.out.println( "   from " + df.format(prime_speed * ms) + " (g/ms) to " + df.format(speed * ms) + " (g/ms)");
		System.out.println( "  Final confidence range:\n   " + df.format(1/rate_low * ms) + " (g/ms) > " + df.format(speed * ms) + " (g/ms) > " + df.format(1/rate_high * ms) + " (g/ms)" );

		System.out.println();

		System.out.println( "  Final rate: \t" + df.format( (rate / ms) ) + " (ms/g)" );
		System.out.println( "  Final speed: \t" + df.format( (speed * ms) ) + " (g/ms)" );
		System.out.println();
		System.out.println( "  Final score: \t" + inf.format( Math.round((speed * ms)) ) + " " );
		
		System.out.println();
	}

}


