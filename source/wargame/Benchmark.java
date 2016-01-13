package wargame;

import java.util.ArrayList;

class Benchmark {

	public static final long MS = 1000000;
	public static final long NS = 1000000000;

	public void benchmark(int threads, double multiplier) {

		WarGameThread[] wgThreads = WarGameThread.create(threads);

		final long DISPLAY_FREQUENCY = NS/10;
		final long SAMPLE_FREQUENCY = NS/200;

		long prime_time = 10000000000l;
		long sample_time = 50000000000l;

		if (multiplier != 1.00) {
			prime_time = (long)((double)prime_time * multiplier);
			sample_time = (long)((double)sample_time * multiplier);
		}

		long end_time = prime_time + sample_time;

		long sample_size = sample_time / SAMPLE_FREQUENCY;

		ArrayList<Double> samples = new ArrayList<Double>((int)sample_size);

		long start_time = System.nanoTime();
		long current_time = start_time;
		long elapsed_time = 0;

		long last_display_time = start_time;
		long last_sample_time = start_time;

		int phase = 1;

		long total_games = 1;

		double speed = 0;

		double maximum_speed = 0;
		double minimum_speed = 0;

		WarGameThread.launch(wgThreads);

		monitor: while (true) {

			total_games = WarGameThread.getCompleted(wgThreads);

			current_time = System.nanoTime();
			elapsed_time = current_time - start_time;

			speed = (double)total_games / (double)elapsed_time;

			if (phase == 1 && elapsed_time >= prime_time) {
				phase = 2;
				maximum_speed = speed;
				minimum_speed = speed;
			} else if (phase == 2) {

				if (maximum_speed < speed) {
					maximum_speed = speed;
				}

				if (minimum_speed > speed) {
					minimum_speed = speed;
				}

				if (elapsed_time >= end_time) {
					phase = 3;
				}

			} else if (phase == 4) {
				break monitor;
			}

			if (phase == 2 && (current_time - last_sample_time) > SAMPLE_FREQUENCY) {
				last_sample_time = current_time;
				samples.add(speed);
			}

			if ((current_time - last_display_time) > DISPLAY_FREQUENCY) {
				last_display_time = current_time;

				if (phase == 1) {
					System.out.printf("\r%d. priming | et = %ds; g = %d; s = %.5f g/ms; \t", phase, elapsed_time / NS, total_games, speed * (double)MS);
				} else if (phase == 2) {
					System.out.printf("\r%d. sampling | et = %ds; g = %d; s = %.5f g/ms; t = %d; \t",
									phase, elapsed_time / NS, total_games, speed * (double)MS, samples.size());
				} else if (phase == 3) {
					phase = 4;
					// intentionally blank line
					System.out.printf("\r%d. done                                                                 \t",
					phase);
				}

			}

		}

		WarGameThread.end(wgThreads);

		final double T_SCORE = 3.291;
		final double ONE_PERCENT = .01;
		final double TEN_PERCENT = .1;

		final double mean = getMean(samples);
		final double median = getMedian(samples);
		final double stdev = getStandardDeviation(samples, mean);
		final double cov = getCoefficientOfVariation(stdev, mean);

		final double mean_median_delta = Math.abs(median - mean);
		final double mm_lower = Math.min(mean, median);
		final double mm_upper = Math.max(mean, median);

		final double min_max_delta = maximum_speed - minimum_speed;
		final double max_ten_percet = maximum_speed * TEN_PERCENT;

		final double one_sigma_lower = mean - stdev;
		final double one_sigma_upper = mean + stdev;
		final double one_sigma_delta = one_sigma_upper - one_sigma_lower;

		final double ci_lower = mean - (T_SCORE * (stdev / Math.sqrt((double)samples.size())));
		final double ci_upper = mean + (T_SCORE * (stdev / Math.sqrt((double)samples.size())));
		final double ci_delta = ci_upper - ci_lower;

		// TODO criteria


		System.out.printf("\n---\n");

		System.out.printf("Samples: %9d\n", samples.size());
		System.out.printf("Mean:\t %9.5f\n", toms(mean));
		System.out.printf("Median:\t %9.5f\n", toms(median));
		System.out.printf("S.D.:\t %9.5f\n", toms(stdev));
		System.out.printf("C.O.V.:\t %9.5f\n", cov);

		System.out.printf("---\n");

		System.out.printf("Min-Max:\t < %9.5f - %9.5f > Δ %9.5f\n",
			toms(minimum_speed),
			toms(maximum_speed),
			toms(min_max_delta));

		System.out.printf("1-σ:\t\t < %9.5f - %9.5f > Δ %9.5f\n",
			toms(one_sigma_lower),
			toms(one_sigma_upper),
			toms(one_sigma_delta));

		System.out.printf("μ-Median:\t < %9.5f - %9.5f > Δ %9.5f\n",
			toms(mm_lower),
			toms(mm_upper),
			toms(mean_median_delta));

		System.out.printf("99.9%% CI:\t < %9.5f - %9.5f > Δ %9.5f\n",
			toms(ci_lower),
			toms(ci_upper),
			toms(ci_delta));


		System.out.printf("---\n");

		System.out.printf("Threads: %d\n", threads);
		System.out.printf("Multiplier: %.2f\n", multiplier);
		System.out.printf("Speed: %.5f g/ms\n", toms(speed));
		System.out.printf("Games: %d\n", total_games);
		System.out.printf("Duration: %.1fs\n", (double)(elapsed_time / NS));

		System.out.printf("---\n");

		// System.out.printf("Rank: (%d/%d) %s\n", rank_passes(criteria), len(criteria), rank_letter(criteria));
		// System.out.printf("Rank Criteria: %s\n", rank_reason(criteria));

		System.out.printf("---\n");

		System.out.printf("Score: %d\n", Math.round(toms(speed)));

	}

	private double toms(Double f) {
		return f * (double)MS;
	}

	private double getMean(ArrayList<Double> samples) {
		double total = 0;
		int size = samples.size();
		for (Double d: samples) {
			total += d;
		}
		return total / (double)size;
	}

	private double getStandardDeviation(ArrayList<Double> samples, double mean) {
		double total = 0;
		int size = samples.size();
		for (Double d: samples) {
			total += Math.pow(d - mean, 2);
		}
		return Math.sqrt(total / (double)(size - 1));
	}

	private double getCoefficientOfVariation(double stdev, double mean) {
		return stdev / mean;
	}

	private double getMedian(ArrayList<Double> samples) {
		ArrayList<Double> s = new ArrayList<Double>(samples);
		int size = s.size();
		double median = 0;
		if (size % 2 == 0) {
			double a = s.get(size / 2 - 1);
			double b = s.get(size / 2 + 1);
			median = (a+b)/2.0;
		} else {
			median = s.get(size / 2);
		}
		return median;
	}

}
