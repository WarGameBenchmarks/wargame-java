package wargame;

import java.util.Scanner;

class SettingsSelector {
	
	private Scanner scanner = new Scanner(System.in);
	private BenchmarkSettings settings;
	
	SettingsSelector(BenchmarkSettings bb) {
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
	
	public void getVariationMagnitude() {
		int temp = 0;
		System.out.print( "  -- Enter confidence threshold [10^-n]\n   (enter 0 for " + this.settings.getVariation_magnitude() + "): " );
		temp = scanner.nextInt();
		if ( 0 != temp ) {
			this.settings.variation_magnitude(temp);
		}
	}
	
	public void getPrimeTime() {
		int temp = 0;
		System.out.print( "  -- Enter priming time\n   (enter 0 for " + this.settings.getPrime_time() + " seconds): " );
		temp = scanner.nextInt();
		if ( 0 != temp ) {
			this.settings.prime_time(temp);
		}
	}
	
	public void getMaximumTests() {
		int temp = 0;
		System.out.print( "  -- Enter maximum tests allowed\n   (enter 0 for " + this.settings.getMaximum_tests() + " tests): " );
		temp = scanner.nextInt();
		if ( 0 != temp ) {
			this.settings.maximum_tests(temp);
		}
	}
	
	public void getUpdateFrequency() {
		int temp = 0;
		System.out.print( "  -- Enter update frequency (frames per second) \n   (enter 0 for " + this.settings.getUpdate_frequency() + " seconds): " );
		System.out.println( "   - default = 1 fps");
		System.out.println( "   - optimal real time = 30 fps");
		temp = scanner.nextInt();
		if ( 0 != temp ) {
			this.settings.update_frequency(temp);
		}
	}
	
}