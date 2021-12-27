package data;

import java.util.ArrayList;
import java.util.List;

//processesOrder should be the arrangement of processes according to execution
//processesTime is time spent in each segment (momken nafs el process aktar men segment aka preemptive)
public class AlgorithmAnswer {
    private final List<Process> processesOrder = new ArrayList<>();
    private final List<Integer> processesTime = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> AgatQuantum;
    private ArrayList<ArrayList<Integer>> AgatFactor;
    private List<Integer> waitingTimesList;//to be assigned from given answer
    private List<Integer> turnAroundTimesList;//to be assigned from given answer

    public void addProcess(Process p, int t) {
        processesOrder.add(p);
        processesTime.add(t);
    }

    public List<Process> getProcessesOrder() {
        return List.copyOf(processesOrder);
    }

    public List<Integer> getProcessesTime() {
        return List.copyOf(processesTime);
    }

    public ArrayList<ArrayList<Integer>> getAgatQuantum() {
        return AgatQuantum;
    }

    public void setAgatQuantum(ArrayList<ArrayList<Integer>> agatQuantum) {
        AgatQuantum = agatQuantum;
    }

    public ArrayList<ArrayList<Integer>> getAgatFactor() {
        return AgatFactor;
    }

    public void setAgatFactor(ArrayList<ArrayList<Integer>> agatFactor) {
        AgatFactor = agatFactor;
    }
    public List<Integer> getWaitingTimesList() {
        return waitingTimesList;
    }

    public void setWaitingTimesList(List<Integer> waitingTimesList) {
        this.waitingTimesList = waitingTimesList;
    }

    public List<Integer> getTurnAroundTimesList() {
        return turnAroundTimesList;
    }

    public void setTurnAroundTimesList(List<Integer> turnAroundTimesList) {
        this.turnAroundTimesList = turnAroundTimesList;
    }
    public double getAverageWaitingTime(){
        double averageTime = 0;
        for (int i :
                waitingTimesList) {
            averageTime+= i;
        }
        return averageTime/waitingTimesList.size();
    }
    public double getAverageTurnAroundTime(){
        double averageTime = 0;
        for (int i :
                turnAroundTimesList) {
            averageTime+= i;
        }
        return averageTime/turnAroundTimesList.size();
    }
}
