package algorithms;

import data.AlgorithmAnswer;
import data.Process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SJF {
    private int completionTime;
    private int turnAroundTime;
    private int waitingTime;
    private final AlgorithmAnswer algorithmAnswer;
    private final List<Process> processes;
    private final List<Integer> waitingTimesList;
    private final List<Integer> turnAroundTimesList;
    HashMap<String, Integer> waits;

    SJF() {
        algorithmAnswer = new AlgorithmAnswer();
        processes = new ArrayList<>();
        waitingTimesList = new ArrayList<>();
        turnAroundTimesList = new ArrayList<>();
        waits = new HashMap<>();
        completionTime = 0;
        turnAroundTime = 0;
        waitingTime = 0;
    }

    public SJF(List<Process> processes) {
        this.processes = processes;
        algorithmAnswer = new AlgorithmAnswer();
        waitingTimesList = new ArrayList<>();
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


    private int findShortJob(int burstTime) {
        int mini = (int) 1e9;
        int index = 0;
        for (int j = 0; j < processes.size(); j++) {
            if (processes.get(j).getArrivalTime() < burstTime) {
                if (mini > processes.get(j).getBurstTime()) {
                    index = j;
                    mini = processes.get(j).getBurstTime();
                }
            }
        }
        return index;
    }

    private int findLongJob(int burstTime) {
        int max = 0;
        int index = 0;
        for (int i = 0; i < processes.size(); i++) {
            if (processes.get(i).getArrivalTime() < burstTime) {
                if (max < processes.get(i).getBurstTime()) {
                    index = i;
                    max = processes.get(i).getBurstTime();
                }
            }
        }
        return index;
    }

    private void addFirstProcess() {
        completionTime = processes.get(0).getBurstTime();
        turnAroundTime = completionTime;
        turnAroundTimesList.add(turnAroundTime);
        algorithmAnswer.addProcess(processes.get(0), processes.get(0).getBurstTime());
    }

    private int addAllTimes(int index, int burstTime) {
        completionTime = burstTime + processes.get(index).getBurstTime();
        turnAroundTime = completionTime - processes.get(index).getArrivalTime();
        waitingTime = turnAroundTime - processes.get(index).getBurstTime();
        waits.replace(processes.get(index).getName(), 0, waitingTime);
        turnAroundTimesList.add(turnAroundTime);
        algorithmAnswer.addProcess(processes.get(index), processes.get(index).getBurstTime());
        burstTime += processes.get(index).getBurstTime();
        processes.remove(index);
        return burstTime;
    }

    public void calculateShortJobFirstWithStarvation() {
        failWaits();
        addFirstProcess();
        int burstTime = processes.get(0).getBurstTime();
        processes.remove(0);
        while (processes.size() > 0) {
            int index = findShortJob(burstTime);
            burstTime = addAllTimes(index, burstTime);
        }
        waitingTimesList.addAll(waits.values());
        algorithmAnswer.setWaitingTimesList(waitingTimesList);
        algorithmAnswer.setTurnAroundTimesList(turnAroundTimesList);
    }

    public void calculateShortJobFirstWithoutStarvation() {
        failWaits();
        int count = 0;
        addFirstProcess();
        int burstTime = processes.get(0).getBurstTime();
        processes.remove(0);
        count++;
        while (processes.size() > 0) {
            int index;
            if (count == 5) {
                count = 0;
                index = findLongJob(burstTime);
            } else {
                index = findShortJob(burstTime);
                count++;
            }
            burstTime = addAllTimes(index, burstTime);
        }
        algorithmAnswer.setWaitingTimesList(waitingTimesList);
    }

    public AlgorithmAnswer getAlgorithmAnswer() {
        return algorithmAnswer;
    }
}
