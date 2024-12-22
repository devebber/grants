import javax.swing.*;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args){
        // Создаем объект DatabaseHandler
        DatabaseHandler dbHandler = new DatabaseHandler();

        //удаление последней строки из бд, так как она пустая(в этом можно убедиться, если зайти в сам csv файл из студии)
        //dbHandler.deleteLastRow();

        // Извлекаем и выводим данные из БД
        //dbHandler.fetchAndPrintGrants();

        //1 задание. Вывод графа
//        Map<Integer, Double> averages = dbHandler.getAverageWorkplacesPerYear();
//        Map<Integer, Double> averagesSort = new TreeMap<>(averages);
//        SwingUtilities.invokeLater(() -> {
//            Graph graph = new Graph(averagesSort);
//            graph.setVisible(true);
//        });

        //2 задание. вывод среднего размера гранта для бизнеса типа "Salon/Barbershop"
//        String businessType = "Salon/Barbershop";
//        double averageGrant = dbHandler.getAverageGrantForBusinessType(businessType);
//
//        if (averageGrant > 0) {
//            System.out.println("Средний размер гранта для бизнеса типа \"" + businessType + "\": $" + averageGrant);
//        } else {
//            System.out.println("Данные для бизнеса типа \"" + businessType + "\" отсутствуют.");
//        }

        //3 задание. вывод типа бизнеса с наиб кол-вом мест при гранте <= 55000
        double maxGrantAmount = 55000.00;
        String businessType = dbHandler.getBusinessTypeWithMaxWorkplacesUnderGrant(maxGrantAmount);

        if (businessType != null) {
            System.out.println("Тип бизнеса с наибольшим количеством рабочих мест при гранте ≤ $" +
                    maxGrantAmount + ": " + businessType);
        } else {
            System.out.println("Данных, соответствующих условию, не найдено.");
        }


    }
}
