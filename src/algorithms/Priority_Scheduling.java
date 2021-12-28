package algorithms;

import data.AlgorithmAnswer;
import data.Process;

import java.util.ArrayList;
import java.util.List;

public class Priority_Scheduling {
    private final AlgorithmAnswer algorithmAnswer;
    private final List<Process> processes;

    public Priority_Scheduling() {
        algorithmAnswer = new AlgorithmAnswer();
        processes = new ArrayList<>();
    }

    public Priority_Scheduling(List<Process> processes) {
        this.processes = processes;
        algorithmAnswer = new AlgorithmAnswer();
    }
    public void addProcess(Process process) {
        processes.add(process);
    }
    private int findHighPriority(){
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
    private int findLowPriority(){
        int max =0;
        int index = 0;
        for (int i = 0; i < processes.size(); i++) {
            if (max < processes.get(i).getPriority()) {
                index = i;
                max = processes.get(i).getPriority();
            }
        }
        return index;
    }
    public void calculatePrioritySchedulingWithStarvation(){
        int burstTime=0;
        while (processes.size() > 0) {
            int index = findHighPriority();
            burstTime+=processes.get(index).getBurstTime();
            algorithmAnswer.addProcess(processes.get(index), burstTime);
            processes.remove(index);
        }
    }
    public void calculatePrioritySchedulingWithoutStarvation(){
        int burstTime=0;
        int count=0;
        int index;
        while (processes.size() > 0) {

            if(count==3){
                count=0;
                index=findLowPriority();
            }else {
                count++;
                index = findHighPriority();
            }
            burstTime+=processes.get(index).getBurstTime();
            algorithmAnswer.addProcess(processes.get(index), burstTime);
            processes.remove(index);
        }
    }
}

