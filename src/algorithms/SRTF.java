package algorithms;

import data.AlgorithmAnswer;
import data.Process;

import java.util.ArrayList;
import java.util.List;

public class SRTF {
    ArrayList<Process> processes;
    int processCount;
    int[] processID;
    int[] arriveTime;
    int[] burstTime ;
    int[] completeTime ;
    int[] turnAround ;
    int[] waitingTime ;
    int[] completeProcess;
    int[] tempBurstTime;
    ArrayList<Integer> order;
    ArrayList<Integer> time;
    double avgWt = 0, avgTa = 0;
    AlgorithmAnswer answer;
   public SRTF(ArrayList<Process> processes){
        this.processes = processes;
        divideProcesses(processes);
    }
    void divideProcesses(ArrayList<Process> processes){
        int i = 0;
        processCount = processes.size();
        processID = new int[processCount];
        arriveTime= new int[processCount];
        burstTime= new int[processCount];
        completeTime= new int[processCount];
        turnAround= new int[processCount];
        waitingTime= new int[processCount];
        completeProcess= new int[processCount];
        tempBurstTime= new int[processCount];
        order = new ArrayList<>();
        time=new ArrayList<>();
        for(Process process : processes){
            processID[i]=i+1;
            arriveTime[i] = process.getArrivalTime();
            burstTime[i] = process.getBurstTime();
            tempBurstTime[i] = burstTime[i];
            completeProcess[i]=0;
            i++;
        }
    }
    public AlgorithmAnswer SRTF(){
        int t = 0, starTime = 0;
        while (true) {
            int min = Integer.MAX_VALUE, c = processCount;
            if (t == processCount) {
                time.add(starTime);
                break;
            }
            for (int i = 0; i < processCount; i++) {
                if (arriveTime[i] <= starTime && completeProcess[i] == 0 && burstTime[i] - 0.6 * waitingTime[i] < min) {
                    min = burstTime[i];
                    c = i;

                }
            }
            if(order.isEmpty()) order.add(c+1);

            else if(order.get(order.size()-1) != c+1 ){
                order.add(c+1);
                time.add(starTime);
            }
            if (c == processCount) starTime++;
            else {
                burstTime[c]--;
                starTime++;
                if (burstTime[c] == 0) {
                    completeTime[c] = starTime;
                    turnAround[c] = completeTime[c] - arriveTime[c];
                    waitingTime[c] = turnAround[c] - tempBurstTime[c];
                    completeProcess[c] = 1;
                    t++;
                }
            }
        }
        List <Integer> wt = new ArrayList<>(),ta = new ArrayList<>();
        for (int i = 0; i < processCount; i++) {
            turnAround[i] = completeTime[i] - arriveTime[i];
            waitingTime[i] = turnAround[i] - tempBurstTime[i];
            wt.add(waitingTime[i]);
            ta.add(turnAround[i]);
            avgWt += waitingTime[i];
            avgTa += turnAround[i];
        }
        answer = new AlgorithmAnswer();
        answer.setWaitingTimesList(wt);
        answer.setTurnAroundTimesList(ta);
        int diff = 0;
        for (int i = 0; i < order.size(); i++) {
            diff = time.get(i)-diff;
            answer.addProcess(processes.get(order.get(i)-1),diff);
            System.out.println(time.get(i));
            diff = time.get(i);
        }

        return answer;
    }

}
