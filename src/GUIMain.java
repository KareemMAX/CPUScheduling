import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GUIMain {
    private JList<String> processesList;
    private JTextField textField1;
    private JComboBox<String> algorithmComboBox;
    private JButton simulateButton;
    private JPanel panel;
    private JButton addButton;
    private JButton removeButton;

    private final List<Process> processes = new ArrayList<>();
    DefaultListModel<String> listModel = new DefaultListModel<>();

    public GUIMain() {
        simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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

    public static void main(String[] args) {
        JFrame frame = new JFrame("CPU Scheduler");
        frame.setContentPane(new GUIMain().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        String[] algorithms ={"Non-Preemptive Priority Scheduling", "SJF", "SRTF", "AGAT"};
        algorithmComboBox = new JComboBox<>(algorithms);
        listModel = new DefaultListModel<>();
        processesList = new JList<>(listModel);
    }
}
