package wargame;

import java.text.*;

class App {

  public static void main(String args[]) {

      int tasks = 1;

      if (args.length == 1) {
        tasks = Integer.parseInt(args[0]);
      }

      System.out.println("settings: tasks = " + tasks);

      Benchmark benchmark = new Benchmark(tasks);
      benchmark.benchmark();

  }

}
