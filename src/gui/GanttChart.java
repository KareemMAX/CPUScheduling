package gui;

import data.AlgorithmAnswer;

import javax.swing.*;
import java.awt.*;

public class GanttChart extends JPanel {
    AlgorithmAnswer answer;

    public GanttChart(AlgorithmAnswer answer) {
        this.answer = answer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(answer == null)
            return;

        int h = getHeight() - 40;
        int w = getWidth() - 40;
        int wPerTime = w / answer.getProcessesTime()
                .stream().reduce(0, Integer::sum);

        Color defaultColor = g.getColor();
        int acc = 0;
        for (int i = 0; i < answer.getProcessesOrder().size(); i++) {
            g.setColor(answer.getProcessesOrder().get(i).getColor());
            g.fillRect(20 + acc, 20, wPerTime * answer.getProcessesTime().get(i), h);
            g.setColor(defaultColor);
            g.drawRect(20 + acc, 20, wPerTime * answer.getProcessesTime().get(i), h);
            g.drawString(answer.getProcessesOrder().get(i).getName(), 30 + acc, getHeight()/2);

            acc += answer.getProcessesTime().get(i) * wPerTime;
        }
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(200, 100);
    }
}
