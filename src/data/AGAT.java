package data;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static java.lang.Math.*;

public class AGAT {

    HashSet<Process> deadList;
    ArrayList<Process> readyProcesses;
    ArrayList<Process> ProcessList;
    Process activeProcess;
    int time, currentQuantumTime, lastArrivalTime;
    double v1;
    AlgorithmAnswer algorithmAnswer;
    boolean preemptiveOfActiveProcess;
    ArrayList<ArrayList<Integer>> agatQuantam = new ArrayList<>();
    ArrayList<ArrayList<Integer>> agatFactor = new ArrayList<>();
    HashMap<String, Integer> hashMap;
    HashMap<Process,Integer> agat;

    AGAT(ArrayList<Process> processes) {
        this.ProcessList = processes;
        time = 0;
        currentQuantumTime = 0;
        lastArrivalTime = 0;
        preemptiveOfActiveProcess = false;
        readyProcesses = new ArrayList<>();
        deadList = new HashSet<>();
        algorithmAnswer = new AlgorithmAnswer();
        hashMap = new HashMap<>();
        v1 = 0;
        agat = new HashMap<>();
    }

    private void calculateV1() {
        double arrivalTime = this.lastArrivalTime;
        if (arrivalTime  >10) v1 = arrivalTime/10;
        else v1 = 1.0;
    }

    private Double calculateV2() {
        int maxBrustTime = 0;
        for (int i = 0; i < ProcessList.size(); ++i) {
            maxBrustTime = max(maxBrustTime, ProcessList.get(i).getBurstTime());
        }
        double brustTime = maxBrustTime;
        if (brustTime > 10)
            return brustTime/10;
        else return 1.0;
    }

    private void setActiveProcess() {
        Process actPro = null;
        int actAgat= Integer.MAX_VALUE;
        if(!readyProcesses.isEmpty()) {
            for (Process process : readyProcesses) {
                if (actAgat > agat.get(process)) {
//                    actAgat = agatFactor(process);
                   actAgat= agat.get(process);
                    actPro = process;
                }
                //agatFactor.get(hashMap.get(process.getName())).add(agatFactor(process));

            }
            if (activeProcess == null) {
                activeProcess = readyProcesses.get(0);
                readyProcesses.remove(activeProcess);
                currentQuantumTime = activeProcess.getQuantum();
                preemptiveOfActiveProcess = false;

            } else{
                //agatFactor.get(hashMap.get(activeProcess.getName())).add(agatFactor(activeProcess));
                if (actAgat < agat.get(activeProcess) || currentQuantumTime == 0){
                    if (currentQuantumTime == 0) {
                        algorithmAnswer.addProcess(activeProcess, activeProcess.getQuantum());
                        activeProcess.setQuantum(activeProcess.getQuantum() + 2);
                        agatQuantam.get(hashMap.get(activeProcess.getName())).add(activeProcess.getQuantum());
                        readyProcesses.add(activeProcess);
                        activeProcess = readyProcesses.get(0);
                    } else {
                        algorithmAnswer.addProcess(activeProcess, activeProcess.getQuantum() - currentQuantumTime);
                        activeProcess.setQuantum(activeProcess.getQuantum() + currentQuantumTime);
                        agatQuantam.get(hashMap.get(activeProcess.getName())).add(activeProcess.getQuantum());
                        readyProcesses.add(activeProcess);
                        activeProcess = actPro;
                    }
                    readyProcesses.remove(activeProcess);
                    currentQuantumTime = activeProcess.getQuantum();
                    preemptiveOfActiveProcess = false;
                }
            }
        }
    }

    private void calculateAgate() {
        for (int i = 0; i < ProcessList.size(); ++i) {
            agat.put(ProcessList.get(i), agatFactor(ProcessList.get(i)));
        }
    }

    private Integer agatFactor(Process process) {
        Integer priority = process.getPriority();
        Integer arrivalTime = process.getArrivalTime();
        Integer brustTime = process.getBurstTime();
        Double v2 = calculateV2();
        Integer ret = (10 - priority) + (int)ceil(arrivalTime / v1) + (int)ceil(brustTime / v2);

        return ret;
    }



    private void checkArrivalTime() {
        for (int i = 0; i < ProcessList.size(); ++i) {
            if (ProcessList.get(i).getArrivalTime() == time) {
                readyProcesses.add(ProcessList.get(i));
                agatQuantam.get(i).add(ProcessList.get(i).getQuantum());
            }
        }
    }

    private void createLists() {
        for (int i = 0; i < ProcessList.size(); ++i) {
            hashMap.put(ProcessList.get(i).getName(), i);
            agatQuantam.add(new ArrayList<Integer>());
            agatFactor.add(new ArrayList<Integer>());
            agat.put(ProcessList.get(i), agatFactor(ProcessList.get(i)));
        }

    }

    private void removeFromReadyProcess(Process process) {
        readyProcesses.remove(process);
    }

    private void addToDeadList(Process process) {
        deadList.add(process);
    }

    private void updateActiveProcess() {
        if (activeProcess == null)
            return;
        activeProcess.setBurstTime(activeProcess.getBurstTime() - 1);
        --currentQuantumTime;
        activeProcess.setProcessTotalTime(activeProcess.getProcessTotalTime() + 1);
        if (!preemptiveOfActiveProcess ) {
            int t = activeProcess.getQuantum() - currentQuantumTime;
            if (t >= round(0.4 * activeProcess.getQuantum())) {
                preemptiveOfActiveProcess = true;
            }
        }
        if (activeProcess.getBurstTime() == 0) {
            algorithmAnswer.addProcess(activeProcess, activeProcess.getQuantum() - currentQuantumTime);
            deadList.add(activeProcess);
            activeProcess = null;
        }
    }

    private void setLastArrivalTime() {
        for (int i = 0; i < ProcessList.size(); ++i) {
            lastArrivalTime = max(lastArrivalTime, ProcessList.get(i).getArrivalTime());
        }
    }

    private void updateWaitingTime() {
        for (int i = 0; i < readyProcesses.size(); ++i) {
            readyProcesses.get(i).setWaitTime(readyProcesses.get(i).getWaitTime() + 1);
        }
    }

    public void run() {


        createLists();
        setLastArrivalTime();
        calculateV1();
        while (time <= lastArrivalTime || activeProcess != null || readyProcesses.size() != 0) {
            checkArrivalTime();
            if (activeProcess == null || preemptiveOfActiveProcess) {
                setActiveProcess();

            }
            calculateAgate();
            for (int i = 0; i < ProcessList.size(); ++i) {
                if (deadList.contains(ProcessList.get(i)))
                    continue;
                agatFactor.get(i).add(agatFactor(ProcessList.get(i)));
            }
            updateActiveProcess();
            updateWaitingTime();
            time++;
        }
        ArrayList<Integer> waitingList = new ArrayList<>();
        ArrayList<Integer> turnAroundList = new ArrayList<>();
        for (int i = 0; i < ProcessList.size(); ++i) {
            waitingList.add(ProcessList.get(i).getWaitTime());
            turnAroundList.add(ProcessList.get(i).getWaitTime() + ProcessList.get(i).getProcessTotalTime());
        }
        algorithmAnswer.setWaitingTimesList(waitingList);
        algorithmAnswer.setTurnAroundTimesList(turnAroundList);
        algorithmAnswer.setAgatQuantum(agatQuantam);
        algorithmAnswer.setAgatFactor(agatFactor);

    }


}
