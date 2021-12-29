package algorithms;

import data.AlgorithmAnswer;
import data.Process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Priority_Scheduling {
    private int completionTime;
    private int turnAroundTime;
    private int waitingTime;
    private final AlgorithmAnswer algorithmAnswer;
    private final List<Process> processes;
    private final List<Integer> waitingTimesList;
    private final List<Integer> turnAroundTimesList;
    private final HashMap<String, Integer> waits;


    public Priority_Scheduling() {
        waitingTimesList = new ArrayList<>();
        algorithmAnswer = new AlgorithmAnswer();
        processes = new ArrayList<>();
        turnAroundTimesList = new ArrayList<>();
        waits = new HashMap<>();
        completionTime = 0;
        turnAroundTime = 0;
        waitingTime = 0;

    }

    public Priority_Scheduling(List<Process> processes) {
        this.processes = processes;
        waitingTimesList = new ArrayList<>();
        algorithmAnswer = new AlgorithmAnswer();
        turnAroundTimesList = new ArrayList<>();
        waits = new HashMap<>();
        completionTime = 0;
        turnAroundTime = 0;
        waitingTime = 0;
    }

    private void failWaits() {
        for (Process process : processes) {
            waits.put(process.getName(), 0);
        }
    }


    private int findHighPriority() {
        int mini = (int) 1e9;
        int index = 0;
        for (int j = 0; j < processes.size(); j++) {
            if (mini > processes.get(j).getPriority()) {
                index = j;
                mini = processes.get(j).getPriority();
            }
        }
        return index;
    }

    private int findLowPriority() {
        int max = 0;
        int index = 0;
        for (int i = 0; i < processes.size(); i++) {
            if (max < processes.get(i).getPriority()) {
                index = i;
                max = processes.get(i).getPriority();
            }
        }
        return index;
    }

    private int addAllTimes(int index, int burstTime) {
        completionTime = burstTime + processes.get(index).getBurstTime();
        turnAroundTime = completionTime - processes.get(index).getArrivalTime();
        waitingTime = burstTime;
        turnAroundTimesList.add(turnAroundTime);
        waits.replace(processes.get(index).getName(), 0, waitingTime);
        algorithmAnswer.addProcess(processes.get(index), processes.get(index).getBurstTime());
        burstTime += processes.get(index).getBurstTime();
        processes.remove(index);
        return burstTime;
    }

    public void calculatePrioritySchedulingWithStarvation() {
        failWaits();
        int burstTime = 0;
        while (processes.size() > 0) {
            int index = findHighPriority();
            burstTime = addAllTimes(index, burstTime);
        }
        waitingTimesList.addAll(waits.values());
        algorithmAnswer.setWaitingTimesList(waitingTimesList);
        algorithmAnswer.setTurnAroundTimesList(turnAroundTimesList);
    }

    public void calculatePrioritySchedulingWithoutStarvation() {
        failWaits();
        int burstTime = 0;
        int count = 0;
        int index;
        while (processes.size() > 0) {
            if (count == 3) {
                count = 0;
                index = findLowPriority();
            } else {
                count++;
                index = findHighPriority();
            }
            burstTime = addAllTimes(index, burstTime);
        }
        waitingTimesList.addAll(waits.values());
        algorithmAnswer.setWaitingTimesList(waitingTimesList);
        algorithmAnswer.setTurnAroundTimesList(turnAroundTimesList);
    }

    public AlgorithmAnswer getAlgorithmAnswer() {
        return algorithmAnswer;
    }
}

