package algorithms;

import data.AlgorithmAnswer;
import data.Process;

import java.util.ArrayList;
import java.util.List;

public class SJF {
    private final AlgorithmAnswer algorithmAnswer;
    private final List<Process> processes;
    List<Integer> waitingTimesList;

    SJF() {
        algorithmAnswer = new AlgorithmAnswer();
        processes = new ArrayList<>();
        waitingTimesList=new ArrayList<>();
    }

    SJF(List<Process> processes) {
        this.processes = processes;
        algorithmAnswer = new AlgorithmAnswer();
        waitingTimesList=new ArrayList<>();
    }

    public void addProcess(Process process) {
        processes.add(process);
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

    public void calculateShortJobFirstWithStarvation() {
        waitingTimesList.add(0);
        algorithmAnswer.addProcess(processes.get(0), processes.get(0).getBurstTime());
        waitingTimesList.add(0);
        int burstTime = processes.get(0).getBurstTime();
        processes.remove(0);
        while (processes.size() > 0) {
            int index = findShortJob(burstTime);
            algorithmAnswer.addProcess(processes.get(index), burstTime);
            waitingTimesList.add(0);
            burstTime += processes.get(index).getBurstTime();
            processes.remove(index);
        }
        algorithmAnswer.setWaitingTimesList(waitingTimesList);
    }

    public void calculateShortJobFirstWithoutStarvation() {
        int count = 0;
        waitingTimesList.add(0);
        int burstTime = processes.get(0).getBurstTime();
        algorithmAnswer.addProcess(processes.get(0), burstTime);
        count++;
        processes.remove(0);
        while (processes.size() > 0) {
            int index;
            if (count == 5) {
                count = 0;
                index = findLongJob(burstTime);
            } else {
                index = findShortJob(burstTime);
                count++;
            }
            waitingTimesList.add(burstTime);
            algorithmAnswer.addProcess(processes.get(index), burstTime);
            burstTime += processes.get(index).getBurstTime();
            processes.remove(index);
        }
        algorithmAnswer.setWaitingTimesList(waitingTimesList);
    }


}
