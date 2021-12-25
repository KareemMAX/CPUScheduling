import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NewProcessDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameTxt;
    private JButton chooseColorButton;
    private JTextField arrivalTxt;
    private JTextField burstTxt;
    private JTextField priorityTxt;
    private JPanel colorPanel;
    private final NewProcessListener listener;

    public Color processColor = Color.RED;

    public NewProcessDialog(NewProcessListener listener) {
        this.listener = listener;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        chooseColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
                colorPanel.setBackground(processColor);
            }
        });
    }

    private void onOK() {
        listener.add(
                new Process(
                        nameTxt.getText(),
                        processColor,
                        Integer.parseInt(arrivalTxt.getText()),
                        Integer.parseInt(burstTxt.getText()),
                        Integer.parseInt(priorityTxt.getText())
                )
        );
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface NewProcessListener {
        void add(Process process);
    }
}
