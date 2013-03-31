class BenchmarkSettings {

	private int available_threads;
	private double percent_variation;
	private long prime_time;

}

class Benchmark {

	private final long ms = 1000000L;
	private final long ns = 1000000000L;

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


	private BenchmarkSettings settings;

	public static long getTime() {
		return System.nanoTime();
	}



	public Benchmark(BenchmarkSettings settings) {

		this.settings = settings;

	}

	public void start() {

		for (int i = 0; i < this.settings.available_threads; i++) {
			this.threads = new WarGameThread();
		}

		this.start_time = getTime();
		WarGameThread.launch(this.threads);
		this.test();
		this.end_time = getTime();

	}

	private void test() {

		while ( WarGameThread.isAlive(this.threads) ) {

			completed = WarGameThread.getComplete(threads);
			current_time = getTime();
			elapsed_time = current_time - start_time;

			rate = elapsed_time / completed;
			speed = 1 / rate;

			if ( !test_started && elapsed_time >= prime_time ) {
				test_started = true;
				next();
				prime_speed = speed;
			} else if ( test_started && elapsed_time >= test_started ) {

				if (rate_low < rate && rate < rate_high || tests >= 0) {
					WarGameThread.terminateThreads(threads);
				} else {
					next();
					tests++;
				}

			}

			if ( (current-time) > (1000 * ms) ) {

				print_last = current_time;
			}

		}

	}

	private void next() {
		test_duration = (long)(1 + Math.ceil( (speed * ms) ));
		test_initial = elapsed_time + ( test_duration * ns );

		percent_rate = rate * percent_variation;

		rate_low = rate - percent_rate;
		rate_high = rate + percent_rate;
	}



}