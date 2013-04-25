class BenchmarkPrinter {
	private Benchmark benchmark;
	BenchmarkPrinter(Benchmark b) {
		this.benchmark = b;
	}
	public void print() {
		
	}
	public Benchmark getBenchmark() {
		return benchmark;
	}
}

class Benchmark {

	public static final long MS = 1000000L;
	public static final long NS = 1000000000L;

	private WarGameThread[] threads;

	private long tests = 1;

	private boolean test_started = false;

	private long
			elapsed_time,
			current_time,
			test_time,
			test_duration,
			start_time,
			end_time,
			last_print;

	private double
			completed,
			speed,
			rate,
			rate_low,
			rate_high,
			prime_speed,
			percent_rate;


	private BenchmarkBuilder settings;
	private BenchmarkPrinter printer;

	public static long getTime() {
		return System.nanoTime();
	}

	public Benchmark(BenchmarkBuilder settings) {

		this.settings = settings;
		this.threads = new WarGameThread[this.settings.getAvailable_threads()];

	}

	public void attachPrinter(BenchmarkPrinter p) {
		this.printer = p;
	}
	
	public void start() {

		this.run();

		this.start_time = getTime();
		WarGameThread.launch(this.threads);
		this.test();
		this.end_time = getTime();

	}

	private void run() {
		for (int i = 0; i < this.settings.getAvailable_threads(); i++) {
			this.threads[i] = new WarGameThread();
		}
	}

	private void test() {

		while ( WarGameThread.isAlive(this.threads) ) {

			completed = WarGameThread.getCompleted(threads);
			
			current_time = getTime();
			elapsed_time = current_time - start_time;

			rate = elapsed_time / completed;
			speed = 1 / rate;

			if ( !test_started && elapsed_time >= settings.getPrime_time() ) {
				test_started = true;
				next();
				prime_speed = speed;
			} else if ( test_started && elapsed_time >= test_time ) {

				if (rate_low < rate && rate < rate_high || tests >= 0) {
					WarGameThread.terminateThreads(threads);
				} else {
					next();
					tests++;
				}

			}

			if ( ( current_time - last_print ) > (1000 * MS) ) {

				this.printer.print();
				last_print = current_time;
				
			}

		}

	}

	private void next() {
		test_duration = (long)(1 + Math.ceil( (speed * MS) ));
		test_time = elapsed_time + ( test_duration * NS );

		percent_rate = rate * settings.getPercent_variation();

		rate_low = rate - percent_rate;
		rate_high = rate + percent_rate;
	}

	public WarGameThread[] getThreads() {
		return threads;
	}

	public long getTests() {
		return tests;
	}

	public boolean isTest_started() {
		return test_started;
	}

	public long getElapsed_time() {
		return elapsed_time;
	}

	public long getCurrent_time() {
		return current_time;
	}

	public long getTest_time() {
		return test_time;
	}

	public long getTest_duration() {
		return test_duration;
	}

	public long getStart_time() {
		return start_time;
	}

	public long getEnd_time() {
		return end_time;
	}

	public long getLast_print() {
		return last_print;
	}

	public double getCompleted() {
		return completed;
	}

	public double getSpeed() {
		return speed;
	}

	public double getRate() {
		return rate;
	}

	public double getRate_low() {
		return rate_low;
	}

	public double getRate_high() {
		return rate_high;
	}

	public double getPrime_speed() {
		return prime_speed;
	}

	public double getPercent_rate() {
		return percent_rate;
	}

	public BenchmarkBuilder getSettings() {
		return settings;
	}



}