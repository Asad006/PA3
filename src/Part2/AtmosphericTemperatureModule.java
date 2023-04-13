package Part2;

import java.util.*;
import java.util.concurrent.Semaphore;

public class AtmosphericTemperatureModule {
    static double computeTime = 0;

    public static void main(String[] args) {
        final int NUMBER_OF_THREADS = 8;


        System.out.print("Enter an integer value for the number of hours.\n");
        //Scanner object
        Scanner input = new Scanner(System.in);
        while (!input.hasNextInt()) {
            input.nextLine();

        }

        int numberOfHours = input.nextInt();

        input.close();

        final long startTime = System.currentTimeMillis();
        for (int i = 0; i < numberOfHours; i++) {

            DataModel dataModel = new DataModel();

            List<sensor> threadsArray = new ArrayList<>();

            //add threads to the list and start
            for (int j = 0; j < NUMBER_OF_THREADS; j++) {
                sensor thread = new sensor(dataModel, j);
                threadsArray.add(thread);
                thread.start();
            }

            //Join
            for (sensor thread : threadsArray) {
                try {
                    thread.join();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            final long endTime = System.currentTimeMillis();
            computeTime = (endTime - startTime) / 1000.0;

            printReport(i, dataModel.maxList, dataModel.minList, dataModel.maxDifference, dataModel.intervalStart, dataModel.intervalEnd);

        }
        System.out.println("Execution time: " + computeTime + " s");
    }

    private static void printReport(int i, PriorityQueue<Integer> maxList, PriorityQueue<Integer> minList, int maxDifference, int intervalStart, int intervalEnd) {
        System.out.println("******************************");
        System.out.println("Sensory Report On Hour: " + (i + 1));
        System.out.println("------------------------------");
        System.out.println("Top 5  temperature: " + maxList);
        System.out.println("Min 5  temperature: " + minList);
        System.out.println("Largest temperature range difference of " + maxDifference + "F was from " + intervalStart + " to " + intervalEnd + " minute\n");
        System.out.println("******************************\n\n");
    }
}

class DataModel {
    public Semaphore lock;
    public PriorityQueue<Integer> maxList;
    public PriorityQueue<Integer> minList;
    public int maxRange;
    public int minRange;

    public int maxDifference;
    public int intervalEnd;
    public int intervalStart;


    public DataModel() {

        maxList = new PriorityQueue<>();
        minList = new PriorityQueue<>((a, b) -> (b - a));
        maxRange = Integer.MIN_VALUE;
        minRange = Integer.MAX_VALUE;
        lock = new Semaphore(1);
        maxDifference = Integer.MIN_VALUE;

    }

    public void dataManager(int temp, int minute) {

        while (!lock.tryAcquire()) {
        }
        try {

            //add temp to max list and keep 5 records.
            maxList.add(temp);
            if (maxList.size() > 5) {
                maxList.poll();
            }

            //add temp to min list and keep 5 records.
            minList.add(temp);
            if (minList.size() > 5) {
                minList.poll();
            }

            // get max and min  temperature from the max and min list.
            maxRange = Math.max(maxRange, temp);
            minRange = Math.min(minRange, temp);

            // check for the end of the interval.
            if (minute % 10 == 6) {
                int rangeDifference = maxRange - minRange;
                if (maxDifference < rangeDifference) {
                    maxDifference = rangeDifference;
                    intervalEnd = minute;
                    intervalStart = intervalEnd - 9;
                }
                maxRange = Integer.MIN_VALUE;
                minRange = Integer.MAX_VALUE;
            }

        } finally {
            lock.release();
        }

    }
}

class sensor extends Thread {
    final int MINUTES = 60;
    public int id;
    public DataModel model;


    public sensor(DataModel model, int id) {

        this.id = id;
        this.model = model;

    }


    @Override
    public void run() {
        //loop over each minute in an hour.
        for (int minute = 0; minute < MINUTES; minute++) {

            //make the thread sleep
            try {
                Thread.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            }

            int temperature = (int) (Math.random() * 171) - 100;

            model.dataManager(temperature, minute);
        }

    }

}