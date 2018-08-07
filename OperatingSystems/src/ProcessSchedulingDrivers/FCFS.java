package ProcessSchedulingDrivers;

import java.util.ArrayList;
import java.util.Scanner;

import Job.Job;

/**
 * FCFS program Driver. Program to implement a First-Come First-Served process
 * Scheduling of an Operating System's job or process.
 *
 * @author Jonnie Klein Quezada
 * @version 1
 */
public class FCFS {

    public static void main(String[] args) {
        System.out.println("First-Come First-Served Process Scheduling\n" +
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

            current.setStartTime(calculateStart(jobList.size(), jobList));
            current.setFinishTime(current.getStartTime() + current.getBurstTime());

            current.setTurnAroundTime(current.getFinishTime() - current.getArrivalTime());

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
        int start = 0;

        for(int i = 0; i < position; i++) {
            start += jobList.get(i).getBurstTime();
        }

        return start;
    }

    /**
     * Prints a job table describing the jobs state and description after the
     * process scheduling. It prints out the job Id, arrival time, CPU cycles,
     * starting time, finish time and finally turnaround time
     *
     * @param jobList The ArrayList containing all the jobs
     */
    private static void printJobTable(ArrayList<Job> jobList) {
        System.out.println("Job.Job Id  |  Arrival  |  Burst  |  Start  |  Finish  |  Turn Around\n" +
                           "-----------------------------------------------------------------");

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
