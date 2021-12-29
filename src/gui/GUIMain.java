package gui;

import algorithms.AGAT;
import algorithms.Priority_Scheduling;
import algorithms.SJF;
import data.AlgorithmAnswer;
import data.Process;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GUIMain {
    private JList<String> processesList;
    private JComboBox<String> algorithmComboBox;
    private JButton simulateButton;
    private JPanel editPanel;
    private JButton addButton;
    private JButton removeButton;
    private JTable processesAnswerTable;
    private JPanel panel;
    private JLabel avgWaitingTimeLabel;
    private JLabel avgTurnaroundTimeLabel;
    private JList<String> agatQuantumProcessList;
    private JList<String> agatQuantumList;
    private JList<String> agatFactorProcessList;
    private JList<String> agatFactorList;
    private JPanel ganttChart;

    private final List<Process> processes = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private AlgorithmAnswer answer = null;


    public GUIMain() {
        simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO query the actual algorithms

//                answer = new AlgorithmAnswer();
//                for (Process p :
//                        processes) {
//                    answer.addProcess(p, p.getBurstTime());
//                }
//                List<Integer> waitingTimesList = new ArrayList<>();
//                List<Integer> turnAroundTimesList = new ArrayList<>();
//                for (int i = 0; i < processes.size(); i++) {
//                    waitingTimesList.add(ThreadLocalRandom.current().nextInt(1,50));
//                    turnAroundTimesList.add(ThreadLocalRandom.current().nextInt(1,50));
//                }
//                answer.setWaitingTimesList(waitingTimesList);
//                answer.setTurnAroundTimesList(turnAroundTimesList);

                switch (algorithmComboBox.getSelectedIndex()) {
                    case 0 -> { // Non-Preemptive Priority Scheduling
                        Priority_Scheduling p = new Priority_Scheduling(new ArrayList<>(processes));
                        p.calculatePrioritySchedulingWithoutStarvation();

                        answer = p.getAlgorithmAnswer();
                    }
                    case 1 -> { // SJF
                        SJF p = new SJF(new ArrayList<>(processes));
                        p.calculateShortJobFirstWithoutStarvation();

                        answer = p.getAlgorithmAnswer();
                    }
                    case 2 -> { // SRTF

                    }
                    case 3 -> { // AGAT
                        AGAT agat = new AGAT(new ArrayList<>(processes));
                        agat.run();

                        answer = agat.getAlgorithmAnswer();
                    }
                }

                updateGUI();
            }
        });

        agatFactorProcessList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                DefaultListModel<String> factorListModel = new DefaultListModel<>();
                factorListModel.addAll(
                        answer.getAgatFactor().get(e.getFirstIndex())
                                .stream().map(Object::toString).toList()
                );
                agatFactorList.setModel(factorListModel);
            }
        });
        agatQuantumProcessList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                DefaultListModel<String> quantumListModel = new DefaultListModel<>();
                quantumListModel.addAll(
                        answer.getAgatQuantum().get(e.getFirstIndex())
                                .stream().map(Object::toString).toList()
                );
                agatQuantumList.setModel(quantumListModel);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewProcessDialog dialog = new NewProcessDialog(
                        new NewProcessDialog.NewProcessListener() {
                            @Override
                            public void add(Process process) {
                                processes.add(process);
                                listModel.addElement(process.getName());
                                processesList.setModel(listModel);
                            }
                        }
                );

                dialog.pack();
                dialog.setLocationByPlatform(true);
                dialog.setVisible(true);
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(processesList.getSelectedIndex() != -1) {
                    processes.remove(processesList.getSelectedIndex());
                    listModel.remove(processesList.getSelectedIndex());
                    processesList.setModel(listModel);
                }
            }
        });
    }
    public void updateGUI(){
        String[] columns = new String[] {"Process", "Waiting Time", "Turnaround Time"};
        String[][] data = new String[processes.size()][3];
        for (int i = 0; i < processes.size(); i++) {
            data[i][0] = processes.get(i).getName();
            data[i][1] = answer.getWaitingTimesList().get(i).toString();
            data[i][2] = answer.getTurnAroundTimesList().get(i).toString();
        }
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setDataVector(data,columns);
        processesAnswerTable.setModel(tableModel);
        avgTurnaroundTimeLabel.setText(String.valueOf(answer.getAverageTurnAroundTime()));
        avgWaitingTimeLabel.setText(String.valueOf(answer.getAverageWaitingTime()));

        ((GanttChart) this.ganttChart).answer = answer;
        ganttChart.repaint();

        agatFactorProcessList.setModel(listModel);
        agatQuantumProcessList.setModel(listModel);
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame("CPU Scheduler");
        frame.setContentPane(new GUIMain().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1018, 681));
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        String[] algorithms ={"Non-Preemptive Priority Scheduling", "SJF", "SRTF", "AGAT"};
        algorithmComboBox = new JComboBox<>(algorithms);
        listModel = new DefaultListModel<>();
        processesList = new JList<>(listModel);

        processesAnswerTable = new JTable(
                new String[][] {},
                new String[] {"Process", "Waiting Time", "Turnaround Time"}
        );

        ganttChart = new GanttChart(answer);
    }
}
