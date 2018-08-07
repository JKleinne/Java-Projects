package Sort;

import Job.Job;

import java.util.Comparator;

public class SortByArrival implements Comparator<Job> {
    @Override
    public int compare(Job o1, Job o2) {
        return o1.getArrivalTime() - o2.getArrivalTime();
    }
}
