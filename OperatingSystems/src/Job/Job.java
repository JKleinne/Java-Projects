package Job;

/**
 * The Job class represents a process or a job in an operating system. It holds information such as
 * its Id, its arrival time, starting time, finish time, CPU cycles, remaining CPU cycles and
 * turnaround time.
 *
 * @author Jonnie Klein Quezada
 * @version 2
 */
public class Job implements Comparable<Job> {
    private String Id;

    private int arrivalTime;

    private int startTime;
    private int finishTime;

    private int burstTime; //CPU Cycle
    private int remainingBurst;

    private int turnAroundTime;

    public Job(String Id, int arrivalTime, int burstTime) {
        this.Id = Id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingBurst = burstTime;
    }

    public void reduceRemainingBurst() {
        remainingBurst--;
    }

    public void reduceRemainingBurst(int quantum) {
        remainingBurst -= quantum;
    }

    public int getRemainingBurst() {
        return remainingBurst;
    }

    public void setRemainingBurst(int remainingBurst) {
        this.remainingBurst = remainingBurst;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public String getId() {
        return this.Id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    @Override
    public int compareTo(Job o) {
        return this.burstTime - o.burstTime;
    }

    public String toString() {
        return String.format("%-10S %-10S %-10S %-10S %-10S %-10S", Id, arrivalTime, burstTime, startTime,
                                                                finishTime, turnAroundTime);
    }
}
