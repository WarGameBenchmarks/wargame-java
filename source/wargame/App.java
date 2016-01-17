package wargame;

import java.text.*;

class App {

	public static void main(String[] args) {

			System.out.println("WarGame Java");

			int threads = 1;
			double multiplier = 1.00;

			try {
				if (args.length >= 2) {
					multiplier = Double.parseDouble(args[1]);
				}
				if (args.length >= 1) {
					threads = Integer.parseInt(args[0]);
				}
			} catch (Exception e) {
				threads = 1;
				multiplier = 1.00;
			}

			System.out.printf("settings: threads = %d; multiplier = %.2f\n\n", threads, multiplier);

			Benchmark benchmark = new Benchmark();

			benchmark.benchmark(threads, multiplier);

	}

}
