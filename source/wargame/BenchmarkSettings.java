package wargame;

class BenchmarkSettings {

	private int available_threads = 1;
	
	private int variation_magnitude = -4;
	
	private int prime_time = 60;
	
	private int maximum_tests = 100;
	
	private int update_frequency = 1;

	BenchmarkSettings() {
		this.available_threads = Runtime.getRuntime().availableProcessors();
	}
	
	public BenchmarkSettings available_threads(int at) {
		if (0 == at) this.available_threads = Runtime.getRuntime().availableProcessors();
		else this.available_threads = at;
		return this;
	}

	public BenchmarkSettings variation_magnitude(int x) {
		this.variation_magnitude = -1 * Math.abs(x);
		return this;
	}

	public BenchmarkSettings prime_time(int x) {
		this.prime_time = Math.abs(x);
		return this;
	}
	
	public BenchmarkSettings maximum_tests(int x) {
		this.maximum_tests = ( x < 10 ? 10 : x );
		return this;
	}
	
	public BenchmarkSettings update_frequency(int x) {
		x = Math.abs(x);
		this.update_frequency = ( x <= 1000 && x > 0 ? x : 1 );
		return this;
	}

	public int getAvailable_threads() {
		return available_threads;
	}

	public int getPrime_time() {
		return prime_time;
	}

	public int getVariation_magnitude() {
		return variation_magnitude;
	}

	public int getMaximum_tests() {
		return maximum_tests;
	}

	public int getUpdate_frequency() {
		return update_frequency;
	}

}