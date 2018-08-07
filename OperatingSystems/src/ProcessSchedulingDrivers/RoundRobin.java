package ProcessSchedulingDrivers;

import Job.Job;
import DataStructures.Queue;
import Sort.SortByArrival;

import java.util.*;

/**
 * Round Robin program Driver. Program to implement a Round Robin process
 * Scheduling of an Operating System's job or process.
 *
 * @author Jonnie Klein Quezada
 * @version 1
 */
public class RoundRobin {

    public static void main(String[] args) {
        System.out.println("Round Robin Process Scheduling\n" +
                "------------------------------------------");

        System.out.print("Enter time quantum: ");
        int quantum = new Scanner(System.in).nextInt();

        Queue<Job> jobList = generateJobs();
        ArrayList<Job> processed = processJobs(jobList, quantum);

        printJobTable(processed);
    }

    /**
     * Processes the jobs preemptively using the Round Robin algorithm and
     * returns an ArrayList of completed Jobs with with their respective
     * start, finish and turnaround time.
     *
     * @param jobByArrival Contains the Jobs to process
     * @return ArrayList containing the jobs processed
     */
    public static ArrayList processJobs(Queue<Job> jobByArrival, int quantum) {
        final int HEAD = 0;
        final int InitialNumberOfJobs = jobByArrival.size();

        //Jobs completed
        ArrayList<Job> completedJobs = new ArrayList<>();
        //Jobs that arrived, currently being processed
        ArrayList<Job> jobsBeingProcessed = new ArrayList<>();

        int time = 0;
        Job current = null;

        while(completedJobs.size() < InitialNumberOfJobs) {

            int counter = 0;
            //While the arrival time ob the head of jobByArrival is less than
            //the current time, add it to the jobsBeingProcessed so as to
            //indicate that the job has "arrived"
            while(!jobByArrival.isEmpty() && jobByArrival.lookup().getArrivalTime() <= time) {
                if(counter == 0)
                    jobsBeingProcessed.add(HEAD, jobByArrival.remove());
                else
                    jobsBeingProcessed.add(HEAD + counter, jobByArrival.remove());

                counter++;
            }

            current = jobsBeingProcessed.remove(HEAD);

            //Only case where a Job's initial CPU cycles is equal
            //to its remaining CPU cycles is if it hasn't been processed
            //yet. In which case, set its starting time
            if (current.getRemainingBurst() == current.getBurstTime())
                current.setStartTime(time);

            //If the Job's remaining Cycle is greater than quantum,
            //then the Job's hasn't finished processing and should
            //be placed back to jobsBeingProcessed and increment
            //time by the quantum
            if (current.getRemainingBurst() > quantum) {
                time += quantum;
                current.reduceRemainingBurst(quantum);

                //Before adding the job back to jobsBeingProcessed,
                //Add the Job or Jobs that should have arrived before
                //the next time slice
                while(!jobByArrival.isEmpty() &&jobByArrival.lookup().getArrivalTime() <= time)
                    jobsBeingProcessed.add(jobByArrival.remove());

                jobsBeingProcessed.add(current);
            }
            //If the Job's remaining Cycle is less than quantum, then
            //increment time by the remaining Cycle and set the Job's
            //finish time and set its remaining Cycle to 0. Finally,
            //Add it to the completedJobs
            else {
                time += current.getRemainingBurst();
                current.setRemainingBurst(0);
                current.setFinishTime(time);

                completedJobs.add(current);
            }

        }

        return completedJobs;
    }

    /**
     * Creates and returns an ArrayList of type Job based on
     * the information the user provided.
     *
     * @return An ArrayList of type Job conataining all the jobs the user entered
     */
    public static Queue generateJobs() {
        Scanner scan = new Scanner(System.in);

        Queue<Job> jobList = new Queue<>();

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
     * Prints a job table describing the jobs state and description after the
     * process scheduling. It prints out the job Id, arrival time, CPU cycles,
     * starting time, finish time and finally turnaround time
     *
     * @param jobList The ArrayList containing all the jobs
     */
    private static void printJobTable(ArrayList<Job> jobList) {
        //Sort collection by arrival time
        Collections.sort(jobList, new SortByArrival());

        System.out.println("Job Id  |  Arrival  |  Burst  |  Start  |  Finish  |  Turn Around\n" +
                "-----------------------------------------------------------------");

        jobList.forEach(job -> job.setTurnAroundTime(job.getFinishTime() - job.getArrivalTime()));

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