package algorithms;

import data.AlgorithmAnswer;
import data.Process;

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
    ArrayList<ArrayList<Integer>> agatQuantum = new ArrayList<>();
    ArrayList<ArrayList<Integer>> agatFactor = new ArrayList<>();
    HashMap<String, Integer> hashMap;
    HashMap<Process,Integer> agat;

    public AGAT(ArrayList<Process> processes) {
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
        for (Process process : ProcessList) {
            maxBrustTime = max(maxBrustTime, process.getBurstTime());
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
                        agatQuantum.get(hashMap.get(activeProcess.getName())).add(activeProcess.getQuantum());
                        readyProcesses.add(activeProcess);
                        activeProcess = readyProcesses.get(0);
                    } else {
                        algorithmAnswer.addProcess(activeProcess, activeProcess.getQuantum() - currentQuantumTime);
                        activeProcess.setQuantum(activeProcess.getQuantum() + currentQuantumTime);
                        agatQuantum.get(hashMap.get(activeProcess.getName())).add(activeProcess.getQuantum());
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
        for (Process process : ProcessList) {
            agat.put(process, agatFactor(process));
        }
    }

    private Integer agatFactor(Process process) {
        int priority = process.getPriority();
        int arrivalTime = process.getArrivalTime();
        int burstTime = process.getBurstTime();
        Double v2 = calculateV2();

        return (10 - priority) + (int)ceil(arrivalTime / v1) + (int)ceil(burstTime / v2);
    }



    private void checkArrivalTime() {
        for (int i = 0; i < ProcessList.size(); ++i) {
            if (ProcessList.get(i).getArrivalTime() == time) {
                readyProcesses.add(ProcessList.get(i));
                agatQuantum.get(i).add(ProcessList.get(i).getQuantum());
            }
        }
    }

    private void createLists() {
        for (int i = 0; i < ProcessList.size(); ++i) {
            hashMap.put(ProcessList.get(i).getName(), i);
            agatQuantum.add(new ArrayList<>());
            agatFactor.add(new ArrayList<>());
            agat.put(ProcessList.get(i), agatFactor(ProcessList.get(i)));
        }

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
        for (Process process : ProcessList) {
            lastArrivalTime = max(lastArrivalTime, process.getArrivalTime());
        }
    }

    private void updateWaitingTime() {
        for (Process readyProcess : readyProcesses) {
            readyProcess.setWaitTime(readyProcess.getWaitTime() + 1);
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
        for (Process process : ProcessList) {
            waitingList.add(process.getWaitTime());
            turnAroundList.add(process.getWaitTime() + process.getProcessTotalTime());
        }
        algorithmAnswer.setWaitingTimesList(waitingList);
        algorithmAnswer.setTurnAroundTimesList(turnAroundList);
        algorithmAnswer.setAgatQuantum(agatQuantum);
        algorithmAnswer.setAgatFactor(agatFactor);

    }

    public AlgorithmAnswer getAlgorithmAnswer() {
        return algorithmAnswer;
    }
}
