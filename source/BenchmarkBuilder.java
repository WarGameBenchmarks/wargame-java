
class BenchmarkBuilder {

	private int available_threads = 1;
	
	private double percent_variation = .0001;
	private int variation_magnitude = 4;
	
	private int prime_time = 60;
	
	private int maximum_tests = 100;
	
	private int update_frequency = 1;

	BenchmarkBuilder() {
		this.available_threads = Runtime.getRuntime().availableProcessors();
	}
	
	public BenchmarkBuilder available_threads(int at) {
		if (Runtime.getRuntime().availableProcessors() < at) this.available_threads = Runtime.getRuntime().availableProcessors();
		else this.available_threads = at;
		return this;
	}

	public BenchmarkBuilder variation_magnitude(int x) {
		this.variation_magnitude = x;
		return this;
	}

	public BenchmarkBuilder prime_time(int x) {
		this.prime_time = x;
		return this;
	}
	
	public BenchmarkBuilder maximum_tests(int x) {
		this.maximum_tests = x;
		return this;
	}
	
	public BenchmarkBuilder update_frequency(int x) {
		this.update_frequency = x;
		return this;
	}
	
	public Benchmark build() {
		return new Benchmark(this);
	}

	public int getAvailable_threads() {
		return available_threads;
	}

	public double getPercent_variation() {
		return percent_variation;
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