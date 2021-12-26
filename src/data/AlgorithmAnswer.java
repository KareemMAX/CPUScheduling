package data;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmAnswer {
    private final List<Process> processesOrder = new ArrayList<>();
    private final List<Integer> processesTime = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> AgatQuantum;
    private ArrayList<ArrayList<Integer>> AgatFactor;

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
}
