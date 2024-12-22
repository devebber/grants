import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Graph extends JFrame {
    private final Map<Integer, Double> averages;

    public Graph(Map<Integer, Double> averages) {
        this.averages = averages;
        setTitle("Среднее количество рабочих мест по годам");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Отступы
        int padding = 50;
        int labelPadding = 30;
        int width = getWidth() - 2 * padding;
        int height = getHeight() - 2 * padding;

        // Получаем максимальное значение из данных
        double maxAverage = averages.values().stream().max(Double::compareTo).orElse(1.0);

        // Рисуем оси
        g.drawLine(padding, height + padding, padding, padding); // Y-axis
        g.drawLine(padding, height + padding, width + padding, height + padding); // X-axis

        // Деления по оси Y
        int numYDivisions = 10;
        for (int i = 0; i <= numYDivisions; i++) {
            int y = height + padding - (i * height / numYDivisions);
            g.drawLine(padding - 5, y, padding + 5, y);
            String yLabel = String.format("%.1f", (maxAverage * i / numYDivisions));
            g.drawString(yLabel, padding - labelPadding, y + 5);
        }

        // Рисуем данные
        int barWidth = width / averages.size();
        int xOffset = padding + barWidth / 2;
        int barSpacing = 10;

        int index = 0;
        for (Map.Entry<Integer, Double> entry : averages.entrySet()) {
            int year = entry.getKey();
            double average = entry.getValue();

            // Координаты и высота бара
            int barHeight = (int) ((average / maxAverage) * height);
            int x = xOffset + index * (barWidth + barSpacing);
            int y = height + padding - barHeight;

            // Рисуем бар
            g.setColor(Color.BLUE);
            g.fillRect(x - barWidth / 2, y, barWidth, barHeight);

            // Подписи к году
            g.setColor(Color.BLACK);
            String yearLabel = String.valueOf(year);
            g.drawString(yearLabel, x - barWidth / 4, height + padding + labelPadding);

            index++;
        }
    }
}
