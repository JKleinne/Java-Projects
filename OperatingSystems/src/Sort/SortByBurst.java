package Sort;

import java.util.Comparator;
import Job.Job;

/**
 * SortByBurst is used as a Comparator instance to sort a collection by burst time
 */
public class SortByBurst implements Comparator<Job> {

    /**
     * Returns an int of positive value if o1's burst time is greater than
     * o2's arrival time else, negative.
     *
     * @param o1 Job to compare
     * @param o2 Job to compare
     * @return Positive value if o1's burst time is greater than
     *         o2's arrival time else, negative.
     */
    @Override
    public int compare(Job o1, Job o2) {
        return o1.getBurstTime() - o2.getBurstTime();
    }
}
