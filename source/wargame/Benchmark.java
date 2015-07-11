package wargame;

import java.util.*;

class Benchmark {

  public static final long MS = 1000000L;
	public static final long NS = 1000000000L;

  private WarGameThread[] threads;

  private int tasks;

  public Benchmark(int tasks) {

    this.tasks = tasks;
    threads = new WarGameThread[tasks];

  }

  public void benchmark() {

    create();

    start();

    ArrayList<Double> samples =
      new ArrayList<Double>(10000);

    boolean test_started = false;

    int
      phase = 1,
      tests = 0,
      maximum_tests = 240;

    long
      completed = 0,
      elasped_time = 0,
      current_time = getTime(),
      test_time = 0,
      test_duration = 0,
      start_time = getTime(),
      end_time = 0,
      last_display_time = 0,
      last_sample_time = 0;

    double
      speed = 0,
      speed_v = 0,
      rate = 0,
      prime_time = 60000000000l,
      display_frequency = 50000000l,
      sample_frequency = 5000000l,
      mean = 0,
      stdev = 0,
      cov = 0;


    System.out.println(String.format("\n%d. prime time has started", phase));

    while (true) {
      completed = WarGameThread.getCompleted(threads);

      current_time = getTime();
      elasped_time = current_time - start_time;

      rate = elasped_time / completed;

      speed = 1 / rate;
      speed_v = speed * MS;

      if (!test_started && elasped_time >= prime_time) {
        test_started = true;
        System.out.println(String.format("\n%d. prime time has ended", phase));
        phase = 2;
        System.out.println(String.format("\n%d. stability testing has started", phase));
      } else if (test_started && elasped_time >= test_time) {

        mean = get_mean(samples);
        stdev = get_standard_deviation(samples, mean);
        cov = get_coefficient_of_variation(mean, stdev);

        // backprint(String.format("mean = %.5f; stdev = %.5f; cov = %.5f; N = %d; t = %d", mean, stdev, cov, samples.size(), tests));

        if (cov <= 1.0 || tests >= maximum_tests) {
          break;
        } else {
          test_duration = 1;
          test_time = elasped_time + (test_duration * NS);
        }
        tests++;
      }

      if ((current_time - last_sample_time) > sample_frequency) {
        last_sample_time = current_time;
        samples.add(speed_v);
      }

      if ((current_time - last_display_time) > display_frequency) {
        last_display_time = current_time;

        if (phase == 1) {
          backprint(
            String.format("%d. et = %d; g = %d; s = %.5f g/ms;\t",
            phase, (int)(elasped_time / NS), completed, speed_v
          ));
        } else {
          backprint(
            String.format("%d. et = %d; g = %d; s = %.5f g/ms; t = %d; v = %.5f%%\t",
            phase, elasped_time / NS, completed, speed_v, tests, ((1.0/cov)*100)
          ));
        }

      }

    }

    System.out.println(String.format("\n%d. stability testing has ended", phase));

    phase = 3;

    stop();
    System.out.println(String.format("\n%d. %d tasks stopped", phase, tasks));


    System.out.println("--- ended");

  }

  private void create() {
    for (int i = 0; i < tasks; i++) {
      threads[i] = new WarGameThread();
    }
  }

  private void start() {
    WarGameThread.launch(threads);
  }

  private void stop() {
    WarGameThread.terminateThreads(threads);
  }

  public static long getTime() {
		return System.nanoTime();
	}

  private double get_mean(ArrayList<Double> samples) {
    double total = 0;
    for (Double d: samples) {
      total += d;
    }
    return total / samples.size();
  }

  private double get_standard_deviation(ArrayList<Double> samples, double mean) {
    double total = 0;
    for (Double d: samples) {
      total += Math.pow((d - mean), 2);
    }
    return Math.sqrt(total / samples.size());
  }

  private double get_coefficient_of_variation(double mean, double stdev) {
    return (stdev / mean) * 100;
  }

  public void backprint(String string) {
    System.out.print("\r" + string);
  }



}
