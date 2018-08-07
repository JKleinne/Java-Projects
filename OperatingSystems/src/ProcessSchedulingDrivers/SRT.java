package ProcessSchedulingDrivers;

import Job.Job;
import Sort.SortByRemainingBurst;
import Sort.SortByArrival;

import java.util.*;

/**
 * SRT program Driver. Program to implement a Shortest Remaining Time process
 * Scheduling of an Operating System's job or process.
 *
 * @author Jonnie Klein Quezada
 * @version 1
 */
public class SRT {

    public static void main(String[] args) {
        System.out.println("Shortest Remaining Time Process Scheduling\n" +
                "------------------------------------------");

        ArrayList<Job> jobList = generateJobs();
        ArrayList<Job> processed = processJobs(jobList);

        printJobTable(processed);
    }

    /**
     * Processes the jobs preemptively using the SRT algorithm and
     * returns an ArrayList of completed Jobs with with their respective
     * start, finish and turnaround time.
     *
     * @param jobByArrival Contains the Jobs to process
     * @return ArrayList containing the jobs processed
     */
    public static ArrayList processJobs(ArrayList<Job> jobByArrival) {
        final int HEAD = 0;
        final int InitialNumberOfJobs = jobByArrival.size();

        //The jobsProcessed PriorityQueue sorts the queue by RemainingBurst,
        //as elements are added to the queue, meaning you don't have to sort
        //the queue each time you add an element
        PriorityQueue<Job> jobsProcessed = new PriorityQueue<>(10, new SortByRemainingBurst());
        ArrayList<Job> completedJobs = new ArrayList<>();

        //Process the first job
        int counter = 1;
        Job current = jobByArrival.remove(HEAD);
        current.reduceRemainingBurst();
        jobsProcessed.add(current);

        while(completedJobs.size() < InitialNumberOfJobs) {

            //If arriving job is less than current job
            if((!jobByArrival.isEmpty()) &&
                    jobByArrival.get(HEAD).getRemainingBurst() < current.getRemainingBurst()) {

                current = jobByArrival.remove(HEAD);
                current.setStartTime(counter);
                jobsProcessed.add(current);
            }
            //If arriving job is greater than or equal than current
            //job, then simply add it to the jobsProcessed queue,
            //BUT set the startingTime at a later date
            else if((!jobByArrival.isEmpty())) {
                Job temp = jobByArrival.remove(HEAD);

                jobsProcessed.add(temp);
            }

            //Get the shortest remaining job
            current = jobsProcessed.remove();

            //Set the startTime of the job that was sent to the
            //jobsProcessed Queue because it was greater than or
            //equal to the current job
            if(current.getRemainingBurst() ==  current.getBurstTime())
                current.setStartTime(counter);

            current.reduceRemainingBurst();

            //If the job is not completed, add it back to the queue,
            //else add it to the completed list and set its finish time
            if(current.getRemainingBurst() != 0) {
                jobsProcessed.add(current);
            }
            else {
                current.setFinishTime(counter + 1);
                completedJobs.add(current);
            }

            counter++;
        }

        return completedJobs;
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
     * Prints a job table describing the jobs state and description after the
     * process scheduling. It prints out the job Id, arrival time, CPU cycles,
     * starting time, finish time and finally turnaround time
     *
     * @param jobList The ArrayList containing all the jobs
     */
    private static void printJobTable(ArrayList<Job> jobList) {
       //Sort job list from smallest burst time to highest
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