package ProcessSchedulingDrivers;

import Job.Job;
import Sort.SortByBurst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * SJN program Driver. Program to implement a Shortest Job Next process
 * Scheduling of an Operating System's job or process.
 *
 * @author Jonnie Klein Quezada
 * @version 1
 */
public class SJN {

    public static void main(String[] args) {
        System.out.println("Shortest Job Next Process Scheduling\n" +
                "------------------------------------------");

        ArrayList<Job> jobList = generateJobs();

        printJobTable(jobList);
    }

    /**
     * Creates and returns an ArrayList of type Job based on
     * the information the user provided.
     *
     * @return An ArrayList of type Job conataining all the jobs the user entered
     */
    public static ArrayList generateJobs() {
        Scanner scan = new Scanner(System.in);
        ArrayList<Job> jobList = new ArrayList<>();

        do {
            System.out.print("\n\nJob.Job Id: ");
            String jobId = scan.next();

            System.out.print("\nArrival Time: ");
            int arrival = scan.nextInt();

            System.out.print("\nCPU cycles(Burst Time): ");
            int burstTime = scan.nextInt();

            Job current = new Job(jobId, arrival, burstTime);

            jobList.add(current);

            System.out.print("Add more Jobs? (Y/N): ");
        } while(scan.next().toUpperCase().equals("Y"));

        return jobList;
    }

    /**
     * Private method for determining a Job's starting time based on
     * the previous Jobs' finish time
     *
     * @param position Position (index) of the Job in the ArrayList
     * @param jobList The ArrayList containing the Job
     * @return The Job's starting time
     */
    private static int calculateStart(int position, ArrayList<Job> jobList) {
        int arrival = 0;

        for(int i = 0; i < position; i++) {
            arrival += jobList.get(i).getBurstTime();
        }

        return arrival;
    }

    /**
     * Sets the start and finish time of the ArrayList containing
     * all the jobs
     * @param jobList The ArrayList containing all the jobs
     */
    private static void setStartAndFinishTime(ArrayList<Job> jobList) {
        for(int i = 0; i < jobList.size(); i++) {
            Job current = jobList.get(i);

            current.setStartTime(calculateStart(i, jobList));
            current.setFinishTime(current.getStartTime() + current.getBurstTime());

            current.setTurnAroundTime(current.getFinishTime() - current.getArrivalTime());
        }
    }

    /**
     * Prints a job table describing the jobs state and description after the
     * process scheduling. It prints out the job Id, arrival time, CPU cycles,
     * starting time, finish time and finally turnaround time
     *
     * @param jobList The ArrayList containing all the jobs
     */
    private static void printJobTable(ArrayList<Job> jobList) {
       //Sort job list from smallest burst time to highest
        Collections.sort(jobList, new SortByBurst());

        System.out.println("Job Id  |  Arrival  |  Burst  |  Start  |  Finish  |  Turn Around\n" +
                "-----------------------------------------------------------------");
        appendFirstArrival(jobList);

        setStartAndFinishTime(jobList);

        jobList.forEach(System.out::println);

        StringBuilder builder = new StringBuilder();
        builder.append("Average Turnaround Time = (");

        jobList.forEach(job -> builder.append(job.getTurnAroundTime() + " + "));

        builder.delete(builder.length() - 3, builder.length() - 1);
        builder.append(") / " + jobList.size() + " = " + avgTurnaround(jobList));

        System.out.println("-----------------------------------------------------------------\n" +
                builder);
    }

    /**
     * Moves the Job with the lowestArrival time to the head of rhe ArrayList
     * @param jobList
     */
    private static void appendFirstArrival(ArrayList<Job> jobList) {
        int pos = 0;
        int lowestArrivavl = 0;

        for(int i = 0; i < jobList.size(); i++) {
            if(jobList.get(i).getArrivalTime() <= lowestArrivavl) {
                pos = i;
                lowestArrivavl = jobList.get(i).getArrivalTime();
            }
        }

        Job temp = jobList.get(pos);
        jobList.remove(pos);
        jobList.add(0, temp);
    }

    /**
     * Calculates and returns the average turnaround time of all the jobs
     *
     * @param jobList The ArrayList containing all the jobs
     * @return The Average turnaround time of all the jobs
     */
    private static double avgTurnaround(ArrayList<Job> jobList) {
        int avg = 0;

        for(Job elem: jobList) {
            avg += elem.getTurnAroundTime();
        }

        return ((double) avg) / jobList.size();
    }

}